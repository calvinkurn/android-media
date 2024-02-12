package com.tokopedia.digital.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingAdditionalConstant
import com.tokopedia.digital.home.domain.DigitalPersoCloseWidgetUseCase
import com.tokopedia.digital.home.model.RechargeHomepageSectionAction
import com.tokopedia.digital.home.model.RechargeHomepageSectionSkeleton
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeTickerHomepageModel
import com.tokopedia.digital.home.presentation.util.RechargeHomepageSectionMapper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.ArrayList

class RechargeHomepageViewModel @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    private val dispatcher: CoroutineDispatchers,
    private val digitalPersoCloseWidgetUseCase: DigitalPersoCloseWidgetUseCase
) :
    BaseViewModel(dispatcher.io) {

    private val mutableRechargeHomepageSectionSkeleton = MutableLiveData<Result<RechargeHomepageSectionSkeleton>>()
    val rechargeHomepageSectionSkeleton: LiveData<Result<RechargeHomepageSectionSkeleton>>
        get() = mutableRechargeHomepageSectionSkeleton

    var localRechargeHomepageSections: List<RechargeHomepageSections.Section> = listOf()

    private val mutableRechargeHomepageSections = MutableLiveData<List<RechargeHomepageSections.Section>>()
    val rechargeHomepageSections: LiveData<List<RechargeHomepageSections.Section>>
        get() = mutableRechargeHomepageSections

    private val mutableRechargeHomepageSectionAction = MutableLiveData<Result<RechargeHomepageSectionAction>>()
    val rechargeHomepageSectionAction: LiveData<Result<RechargeHomepageSectionAction>>
        get() = mutableRechargeHomepageSectionAction

    private val mutableRechargeTickerHomepageModel = MutableLiveData<Result<RechargeTickerHomepageModel>>()
    val rechargeTickerHomepageModel: LiveData<Result<RechargeTickerHomepageModel>>
        get() = mutableRechargeTickerHomepageModel

    val calledSectionIds = hashSetOf<Int>()

    fun getRechargeHomepageSectionSkeleton(mapParams: Map<String, Any>) {
        onRefreshData()
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                QueryRechargeHomepageSkeleton(),
                RechargeHomepageSectionSkeleton.Response::class.java,
                mapParams
            )
            val data = withContext(dispatcher.io) {
                graphqlRepository.response(listOf(graphqlRequest))
            }.getSuccessData<RechargeHomepageSectionSkeleton.Response>().response

            mutableRechargeHomepageSectionSkeleton.postValue(Success(data))
            // Add initial section data
            localRechargeHomepageSections = RechargeHomepageSectionMapper.mapInitialHomepageSections(data.sections)
            mutableRechargeHomepageSections.postValue(localRechargeHomepageSections)
        }) {
            mutableRechargeHomepageSectionSkeleton.postValue(Fail(it))
        }
    }

    fun getRechargeHomepageSections(mapParams: Map<String, Any>) {
        val requestIDs = (mapParams[PARAM_RECHARGE_HOMEPAGE_SECTIONS_SECTION_IDS] as? List<Int>)
            ?: listOf()

        if (calledSectionIds.contains(requestIDs.firstOrNull() ?: 0)) return
        calledSectionIds.add(requestIDs.firstOrNull() ?: 0)
        val mapParamsWithSectionNames =
            if (mapParams[PARAM_RECHARGE_HOMEPAGE_SECTIONS_SECTION_NAME] != "") {
                mapParams.filter {
                    it.key != PARAM_RECHARGE_HOMEPAGE_SECTIONS_SECTION_IDS
                }
            } else {
                mapParams.filter {
                    it.key != PARAM_RECHARGE_HOMEPAGE_SECTIONS_SECTION_NAME
                }
            }

        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                QueryRechargeHomepageSection(),
                RechargeHomepageSections.Response::class.java,
                mapParamsWithSectionNames
            )
            val data = if (mapParams.get(PARAM_RECHARGE_HOMEPAGE_SECTIONS_SECTION_NAME) == "TODO_WIDGET_SECTION") {
                val result = Gson().fromJson(DUMMY_NEW_TRACKER, RechargeHomepageSections.Response::class.java)
                result.response
            } else {
                withContext(dispatcher.io) {
                    graphqlRepository.response(listOf(graphqlRequest))
                }.getSuccessData<RechargeHomepageSections.Response>().response
            }
            data.requestIDs = requestIDs

            /*
                Update local (viewmodel) section then update LiveData in order to
                prevent missing section updates caused by postValue override
             */
            withContext(dispatcher.main) {
                localRechargeHomepageSections = RechargeHomepageSectionMapper.updateSectionsData(localRechargeHomepageSections, data)
                mutableRechargeHomepageSections.value = localRechargeHomepageSections
            }
        }) {
            // Because error occured, remove sections
            withContext(dispatcher.main) {
                localRechargeHomepageSections = RechargeHomepageSectionMapper.updateSectionsData(
                    localRechargeHomepageSections,
                    RechargeHomepageSections(requestIDs = requestIDs)
                )
                mutableRechargeHomepageSections.value = localRechargeHomepageSections
            }
        }
    }

    fun refreshSectionList(section: RechargeHomepageSections.Section) {
        launch {
            calledSectionIds.remove(section.id.toIntOrZero())
            withContext(dispatcher.main) {
                localRechargeHomepageSections = RechargeHomepageSectionMapper.updateSectionList(
                    localRechargeHomepageSections,
                    section
                )
                mutableRechargeHomepageSections.value = localRechargeHomepageSections
            }
        }
    }

    private fun onRefreshData() {
        calledSectionIds.clear()
    }

    fun triggerRechargeSectionAction(mapParams: Map<String, Any>) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                QueryRechargeHomepageAction(),
                RechargeHomepageSectionAction.Response::class.java,
                mapParams
            )
            val data = withContext(dispatcher.io) {
                graphqlRepository.response(listOf(graphqlRequest))
            }.getSuccessData<RechargeHomepageSectionAction.Response>().response

            mutableRechargeHomepageSectionAction.postValue(Success(data))
        }) {
            mutableRechargeHomepageSectionAction.postValue(Fail(it))
        }
    }

    fun getTickerHomepageSection(mapParams: Map<String, Any>) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                QueryRechargeHomepageTicker(),
                RechargeTickerHomepageModel::class.java,
                mapParams
            )
            val data = withContext(dispatcher.io) {
                graphqlRepository.response(listOf(graphqlRequest))
            }.getSuccessData<RechargeTickerHomepageModel>()

            mutableRechargeTickerHomepageModel.postValue(Success(data))
        }) {
            mutableRechargeTickerHomepageModel.postValue(Fail(it))
        }
    }

    fun createRechargeHomepageSectionSkeletonParams(platformId: Int, enablePersonalize: Boolean = false): Map<String, Any> {
        return mapOf(
            PARAM_RECHARGE_HOMEPAGE_SECTIONS_PLATFORM_ID to platformId,
            PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE to enablePersonalize
        )
    }

    fun createRechargeHomepageSectionsParams(platformId: Int, sectionIDs: List<Int>, enablePersonalize: Boolean = false, sectionNames: String): Map<String, Any> {
        return mapOf(
            PARAM_RECHARGE_HOMEPAGE_SECTIONS_PLATFORM_ID to platformId,
            PARAM_RECHARGE_HOMEPAGE_SECTIONS_SECTION_IDS to sectionIDs,
            PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE to enablePersonalize,
            PARAM_RECHARGE_HOMEPAGE_SECTIONS_SECTION_NAME to sectionNames
        )
    }

    fun createRechargeHomepageTickerParams(categoryId: List<Int>, deviceId: Int): Map<String, Any> {
        return mapOf(
            PARAM_RECHARGE_HOMEPAGE_SECTION_CATEGORY_ID to categoryId,
            PARAM_RECHARGE_HOMEPAGE_SECTION_DEVICE_ID to deviceId
        )
    }

    fun createRechargeHomepageSectionActionParams(
        sectionId: Int,
        actionName: String,
        sectionObjectId: String,
        itemObjectId: String
    ): Map<String, Any> {
        return mapOf(
            PARAM_RECHARGE_HOMEPAGE_SECTION_ID to sectionId,
            PARAM_RECHARGE_HOMEPAGE_SECTION_ACTION to String.format(
                Locale.getDefault(),
                "%s:%s:%s",
                actionName,
                sectionObjectId,
                itemObjectId
            )
        )
    }

    fun getDynamicIconsSectionIds(): ArrayList<String> {
        rechargeHomepageSectionSkeleton.value.let {
            return if (it is Success) {
                val dynamicIconSectionsIds = arrayListOf<String>()
                it.data.sections.filter { it.template.equals(SECTION_DYNAMIC_ICONS) }
                    .forEach { dynamicIconSectionsIds.add(it.id) }
                dynamicIconSectionsIds
            } else {
                arrayListOf()
            }
        }
    }

    fun getSearchBarPlaceholder(): String {
        rechargeHomepageSectionSkeleton.value.let {
            return if (it is Success) it.data.searchBarPlaceholder else ""
        }
    }

    fun getSearchBarScreenName(): String = rechargeHomepageSectionSkeleton.value.let {
        if (it is Success) {
            it.data.searchBarScreenName
        } else {
            RechargeHomepageTrackingAdditionalConstant.SCREEN_NAME_TOPUP_BILLS
        }
    }

    fun getSearchBarRedirection(): String = rechargeHomepageSectionSkeleton.value.let {
        if (it is Success) it.data.searchBarRedirection else ""
    }

    fun closeWidgetDigiPerso(favId: String, type: String) {
        launch {
            val data = withContext(dispatcher.io) {
                digitalPersoCloseWidgetUseCase.digitalPersoCloseWidget(
                    favId, type
                )
            }
        }
    }

    companion object {
        const val ID_TICKER = "0"

        const val PARAM_RECHARGE_HOMEPAGE_SECTION_ID = "sectionID"
        const val PARAM_RECHARGE_HOMEPAGE_SECTION_ACTION = "action"
        const val PARAM_RECHARGE_HOMEPAGE_SECTIONS_PLATFORM_ID = "platformID"
        const val PARAM_RECHARGE_HOMEPAGE_SECTIONS_SECTION_IDS = "sectionIDs"
        const val PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE = "enablePersonalize"
        const val PARAM_RECHARGE_HOMEPAGE_SECTIONS_SECTION_NAME = "sectionNames"
        const val PARAM_RECHARGE_HOMEPAGE_SECTION_CATEGORY_ID = "categoryIDs"
        const val PARAM_RECHARGE_HOMEPAGE_SECTION_DEVICE_ID = "deviceID"

        const val SECTION_TOP_BANNER = "TOP_BANNER"
        const val SECTION_TOP_BANNER_EMPTY = "TOP_BANNER_EMPTY"
        const val SECTION_TOP_ICONS = "TOP_ICONS"
        const val SECTION_DYNAMIC_ICONS = "DYNAMIC_ICONS"
        const val SECTION_DUAL_ICONS = "DUAL_ICONS"
        const val SECTION_URGENCY_WIDGET = "URGENCY_WIDGET"
        const val SECTION_VIDEO_HIGHLIGHT = "VIDEO_HIGHLIGHT"
        const val SECTION_VIDEO_HIGHLIGHTS = "VIDEO_HIGHLIGHTS"
        const val SECTION_SINGLE_BANNER = "SINGLE_BANNER"
        const val SECTION_COUNTDOWN_SINGLE_BANNER = "COUNTDOWN_SINGLE_BANNER"
        const val SECTION_DUAL_BANNERS = "DUAL_BANNERS"
        const val SECTION_LEGO_BANNERS = "LEGO_BANNERS"
        const val SECTION_PRODUCT_CARD_ROW = "PRODUCT_CARD_ROW"
        const val SECTION_PRODUCT_CARD_ROW_1X1 = "PRODUCT_CARD_ROW_1X1"
        const val SECTION_COUNTDOWN_PRODUCT_BANNER = "COUNTDOWN_PRODUCT_BANNER"
        const val SECTION_PRODUCT_CARD_CUSTOM_BANNER = "PRODUCT_CARD_CUSTOM_BANNER"
        const val SECTION_MINI_CAROUSELL = "MINI_CAROUSELL"
        const val SECTION_TICKER = "TICKER"
        const val SECTION_SWIPE_BANNER = "SWIPE_BANNER"
        const val SECTION_PRODUCT_CARD_DGU = "PRODUCT_CARD_DGU"
        const val SECTION_3_ICONS = "3_ICONS"
        const val SECTION_PRODUCT_CARD_CUSTOM_BANNER_V2 = "PRODUCT_CARD_CUSTOM_BANNER_V2"
        const val SECTION_RECOMMENDATION_BANNER = "2X2_BANNER"
        const val SECTION_PRODUCT_CARD_CUSTOM_LAST_ITEM = "PRODUCT_CARD_CUSTOM_LAST_ITEM"
        const val SECTION_OFFERING_WIDGET = "OFFERING_WIDGET"
        const val SECTION_MY_BILLS_WIDGET = "BILL_WIDGET"
        const val SECTION_MY_BILLS_ENTRYPOINT_WIDGET = "MYBILLS_ENTRYPOINT"
        const val SECTION_MY_BILLS_TRIPLE_ENTRYPOINT_WIDGET = "3_BUTTONS_MYBILLS_ENTRYPOINT"
        const val SECTION_TODO_WIDGET = "TODO_WIDGET"

        const val ALL_CATEGORY_PLATFORM_ID = 52

        const val DUMMY_NEW_TRACKER = """
            {
    "rechargeGetDynamicPage": {
      "sections": [
        {
          "sections_id": 2950,
          "object_id": "",
          "title": "",
          "sub_title": "",
          "source_name": "dg_perso_channel_name",
          "template": "TODO_WIDGET",
          "web_link": "",
          "media_url": "",
          "label_1": "",
          "label_2": "",
          "text_link": "",
          "tracking": [
            {
              "action": "impression",
              "data": "{\"event\":\"view_item\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"impression todo widget\",\"eventLabel\":\"2950 - new user\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"\",\"item_name\":\"\",\"creative_name\":\"\",\"creative_slot\":\"0\"},{\"item_id\":\"577\",\"item_name\":\"\",\"creative_name\":\"todo reminder postpaid\",\"creative_slot\":\"1\"},{\"item_id\":\"407\",\"item_name\":\"\",\"creative_name\":\"todo reminder postpaid\",\"creative_slot\":\"2\"},{\"item_id\":\"8364\",\"item_name\":\"\",\"creative_name\":\"todo reminder postpaid\",\"creative_slot\":\"3\"},{\"item_id\":\"116\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"4\"},{\"item_id\":\"114\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"5\"},{\"item_id\":\"3015\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"6\"},{\"item_id\":\"1585\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"7\"},{\"item_id\":\"5108\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"8\"},{\"item_id\":\"5142\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"9\"},{\"item_id\":\"577\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"10\"},{\"item_id\":\"407\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"11\"},{\"item_id\":\"5108\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"12\"},{\"item_id\":\"8364\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"13\"},{\"item_id\":\"8364\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"14\"},{\"item_id\":\"559\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"15\"},{\"item_id\":\"393\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"16\"}]}",
              "__typename": "RechargeDynamicPageTracking"
            }
          ],
          "params": [],
          "items": [
            {
              "tracking": [],
              "widgets": [
                {
                  "fav_id": "454901",
                  "title": "Pulsa",
                  "subtitle": "300.000",
                  "label": "081311205111",
                  "icon_url": "https://images.tokopedia.net/img/SnKlQx/2022/2/2/3f060ab5-9a19-43e5-bbe1-d1bd21c274ed.png",
                  "price": "Rp300.000",
                  "price_plain": 300000,
                  "discount": "",
                  "slashed_price": "",
                  "reason": "Pembayaran otomatis perlu PIN GoPay.",
                  "app_link": "tokopedia://digital/form?category_id=1&client_number=081311205111&product_id=116&operator_id=12&is_from_widget=true",
                  "web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=12&product_id=116&client_number=081311205111",
                  "option_buttons": [
                    {
                      "button": "Atur",
                      "web_link": "https://staging.tokopedia.com/mybills/edit?category_id=1&template=telcopre&menu_id=289&product_id=116&client_number=W2Z5cuBLGzr7soanuswxb9lYYl+1Ft/I7emDkk6hM06zEZPppZUYWmhfvSBp2TgWcKiTXgIFN4XzWwZp7qAHXhVJn0DJktYkhrojwyAlLlwNp3fhif92lKIBJ8kSdn1B5iRZ8uGyY9BJ7INi/umgPNrRZiJSqieAVLLVdftPwtMGskVizvxkTjf9+MnwwxAy2aN7fgWqrci/rON6MRx01NwJB4uHk1iJjUuZ7c5y+tm+CsZzeKfeKXK74ETE2YMQevYAliBBhldtgQZXhVQ6QQoyGrVYUA8dcPU0gx/4IsPYRgqzceGmqOAiM46ZiWhdJAzrK75phhjuyInu01ym7w==&source=shp",
                      "app_link": "tokopedia://webview?titlebar=false&url=https%3A%2F%2Fstaging.tokopedia.com%2Fmybills%2Fedit%3Fcategory_id%3D1%26template%3Dtelcopre%26menu_id%3D289%26product_id%3D116%26client_number%3DW2Z5cuBLGzr7soanuswxb9lYYl%2B1Ft%2FI7emDkk6hM06zEZPppZUYWmhfvSBp2TgWcKiTXgIFN4XzWwZp7qAHXhVJn0DJktYkhrojwyAlLlwNp3fhif92lKIBJ8kSdn1B5iRZ8uGyY9BJ7INi%2FumgPNrRZiJSqieAVLLVdftPwtMGskVizvxkTjf9%2BMnwwxAy2aN7fgWqrci%2FrON6MRx01NwJB4uHk1iJjUuZ7c5y%2Btm%2BCsZzeKfeKXK74ETE2YMQevYAliBBhldtgQZXhVQ6QQoyGrVYUA8dcPU0gx%2F4IsPYRgqzceGmqOAiM46ZiWhdJAzrK75phhjuyInu01ym7w%3D%3D%26source%3Dshp",
                      "tracking": [
                        {
                          "data": "{\"event\":\"impression-Button\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 7 - 2950 - new user - 88\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"407\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"7\"}]}",
                          "action": "impression-Button"
                        },
                        {
                          "data": "{\"event\":\"click-Button\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 7 - 2950 - new user - 88\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"407\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"7\"}]}",
                          "action": "click-Button"
                        }
                      ]
                    }
                  ],
                  "button": "Bayar",
                  "button_app_link": "tokopedia://digital/form?category_id=1&client_number=081311205111&product_id=116&operator_id=12&is_from_widget=true",
                  "button_web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=12&product_id=116&client_number=081311205111",
                  "type": "AUTOPAY_ISSUE",
                  "is_close_button": false,
                  "is_sticky": false,
                  "tracking": [
                    {
                      "action": "click",
                      "data": "{\"event\":\"select_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 0 - 2950 - new user - 12\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"116\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"0\"}]}"
                    },
                    {
                      "action": "impression-3dots",
                      "data": "{\"event\":\"impression-3dots\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"impression-3dots\",\"eventLabel\":\"todo widget - 0 - 2950 - new user - 12\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"116\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"0\"}]}"
                    },
                    {
                      "action": "click-3dots",
                      "data": "{\"event\":\"click-3dots\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click-3dots\",\"eventLabel\":\"todo widget - 0 - 2950 - new user - 12\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"116\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"0\"}]}"
                    },
                    {
                      "action": "close",
                      "data": "{\"event\":\"close_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"close\",\"eventLabel\":\"todo widget - 0 - 2950 - new user - 12\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"116\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"0\"}]}"
                    }
                  ]
                },
                {
                  "fav_id": "454701",
                  "title": "Pulsa",
                  "subtitle": "150.000",
                  "label": "081311205111",
                  "icon_url": "https://images.tokopedia.net/img/SnKlQx/2022/2/2/3f060ab5-9a19-43e5-bbe1-d1bd21c274ed.png",
                  "price": "Rp150.000",
                  "price_plain": 150000,
                  "discount": "",
                  "slashed_price": "",
                  "reason": "Pembayaran otomatis perlu PIN GoPay.",
                  "app_link": "tokopedia://digital/form?category_id=1&client_number=081311205111&product_id=114&operator_id=12&is_from_widget=true",
                  "web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=12&product_id=114&client_number=081311205111",
                  "option_buttons": [
                    {
                      "button": "Atur",
                      "web_link": "https://staging.tokopedia.com/mybills/edit?category_id=1&template=telcopre&menu_id=289&product_id=114&client_number=bKIT+mKlcY0Ol9+aKohKp05sd/GCojhs2QsHyjYwn4+xEzsdLfijhULfeFS6lcyjYRsmDcI6uDOI2+tU39YO2c7UOydysI3OIqbsHH0J+hcije0oE2pd9A/ADDKT4g+8nZVxvLV50SFi4LyXDVoO9RH0YgSt6uYx8G8OObdHdOAa2ElWBGU8mHyKno0iNUlBIDd+QYEgjvPSx5/RDpzmvWVPYy7PHqTJRGfq2O8T/SO00g1sEvHqz2YNqQBysr+j+9rPyy7F1Hunfj5Iz1cXJbVghKiyFMeNuFyPtaNJM2dOTVg2Q47N/ICsPZnawlraV/o0LTrEyxl3/nfZ9alozg==&source=shp",
                      "app_link": "tokopedia://webview?titlebar=false&url=https%3A%2F%2Fstaging.tokopedia.com%2Fmybills%2Fedit%3Fcategory_id%3D1%26template%3Dtelcopre%26menu_id%3D289%26product_id%3D114%26client_number%3DbKIT%2BmKlcY0Ol9%2BaKohKp05sd%2FGCojhs2QsHyjYwn4%2BxEzsdLfijhULfeFS6lcyjYRsmDcI6uDOI2%2BtU39YO2c7UOydysI3OIqbsHH0J%2Bhcije0oE2pd9A%2FADDKT4g%2B8nZVxvLV50SFi4LyXDVoO9RH0YgSt6uYx8G8OObdHdOAa2ElWBGU8mHyKno0iNUlBIDd%2BQYEgjvPSx5%2FRDpzmvWVPYy7PHqTJRGfq2O8T%2FSO00g1sEvHqz2YNqQBysr%2Bj%2B9rPyy7F1Hunfj5Iz1cXJbVghKiyFMeNuFyPtaNJM2dOTVg2Q47N%2FICsPZnawlraV%2Fo0LTrEyxl3%2FnfZ9alozg%3D%3D%26source%3Dshp",
                      "tracking": [
                        {
                          "data": "{\"event\":\"impression-Button\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 7 - 2950 - new user - 88\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"4071\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"7\"}]}",
                          "action": "impression-Button"
                        },
                        {
                          "data": "{\"event\":\"click-Button\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 7 - 2950 - new user - 88\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"4071\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"7\"}]}",
                          "action": "click-Button"
                        }
                      ]
                    }
                  ],
                  "button": "Bayar",
                  "button_app_link": "tokopedia://digital/form?category_id=1&client_number=081311205111&product_id=114&operator_id=12&is_from_widget=true",
                  "button_web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=12&product_id=114&client_number=081311205111",
                  "type": "AUTOPAY_ISSUE",
                  "is_close_button": false,
                  "is_sticky": false,
                  "tracking": [
                    {
                      "action": "click",
                      "data": "{\"event\":\"select_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 1 - 2950 - new user - 12\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"114\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"1\"}]}"
                    },
                    {
                      "action": "impression-3dots",
                      "data": "{\"event\":\"impression-3dots\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"impression-3dots\",\"eventLabel\":\"todo widget - 1 - 2950 - new user - 12\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"114\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"1\"}]}"
                    },
                    {
                      "action": "click-3dots",
                      "data": "{\"event\":\"click-3dots\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click-3dots\",\"eventLabel\":\"todo widget - 1 - 2950 - new user - 12\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"114\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"1\"}]}"
                    },
                    {
                      "action": "close",
                      "data": "{\"event\":\"close_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"close\",\"eventLabel\":\"todo widget - 1 - 2950 - new user - 12\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"114\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"1\"}]}"
                    }
                  ]
                },
                {
                  "fav_id": "455201",
                  "title": "Pulsa",
                  "subtitle": "75.000",
                  "label": "081311205111",
                  "icon_url": "https://images.tokopedia.net/img/SnKlQx/2022/2/2/3f060ab5-9a19-43e5-bbe1-d1bd21c274ed.png",
                  "price": "Rp76.500",
                  "price_plain": 76500,
                  "discount": "",
                  "slashed_price": "",
                  "reason": "Pembayaran otomatis berkendala, silakan bayar secara manual.",
                  "app_link": "tokopedia://digital/form?category_id=1&client_number=081311205111&product_id=3015&operator_id=12&is_from_widget=true",
                  "web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=12&product_id=3015&client_number=081311205111",
                  "option_buttons": [],
                  "button": "Bayar",
                  "button_app_link": "tokopedia://digital/form?category_id=1&client_number=081311205111&product_id=3015&operator_id=12&is_from_widget=true",
                  "button_web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=12&product_id=3015&client_number=081311205111",
                  "type": "AUTOPAY_ISSUE",
                  "is_close_button": false,
                  "is_sticky": false,
                  "tracking": [
                    {
                      "action": "click",
                      "data": "{\"event\":\"select_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 2 - 2950 - new user - 12\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"3015\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"2\"}]}"
                    },
                    {
                      "action": "close",
                      "data": "{\"event\":\"close_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"close\",\"eventLabel\":\"todo widget - 2 - 2950 - new user - 12\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"3015\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"2\"}]}"
                    }
                  ]
                },
                {
                  "fav_id": "438202",
                  "title": "m.tix XXI",
                  "subtitle": "Rp 100.000",
                  "label": "GoPay 2",
                  "icon_url": "https://images.tokopedia.net/img/SnKlQx/2022/1/6/1c011526-27c6-4f4f-820b-dac11201a557.png",
                  "price": "Rp105.000",
                  "price_plain": 105000,
                  "discount": "",
                  "slashed_price": "",
                  "reason": "Pembayaran otomatis berkendala, silakan bayar secara manual.",
                  "app_link": "tokopedia://digital/form?category_id=31&client_number=085911229111&product_id=1585&operator_id=338&is_from_widget=true",
                  "web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=338&product_id=1585&client_number=085911229111",
                  "option_buttons": [],
                  "button": "Bayar",
                  "button_app_link": "tokopedia://digital/form?category_id=31&client_number=085911229111&product_id=1585&operator_id=338&is_from_widget=true",
                  "button_web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=338&product_id=1585&client_number=085911229111",
                  "type": "AUTOPAY_ISSUE",
                  "is_close_button": false,
                  "is_sticky": false,
                  "tracking": [
                    {
                      "action": "click",
                      "data": "{\"event\":\"select_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 3 - 2950 - new user - 338\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"1585\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"3\"}]}"
                    },
                    {
                      "action": "close",
                      "data": "{\"event\":\"close_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"close\",\"eventLabel\":\"todo widget - 3 - 2950 - new user - 338\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"1585\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"3\"}]}"
                    }
                  ]
                },
                {
                  "fav_id": "464501",
                  "title": "Biaya Pendidikan",
                  "subtitle": "IPMI International",
                  "label": "6001799111",
                  "icon_url": "https://images.tokopedia.net/img/SnKlQx/2022/1/6/6ad0f8e7-a750-4aa7-bc18-df1813d8236f.png",
                  "price": "",
                  "price_plain": 0,
                  "discount": "",
                  "slashed_price": "",
                  "reason": "Pembayaran otomatis berkendala, silakan bayar secara manual.",
                  "app_link": "tokopedia://digital/form?category_id=52&client_number=6001799111&product_id=5108&operator_id=1816&is_from_widget=true",
                  "web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=1816&product_id=5108&client_number=6001799111",
                  "option_buttons": [],
                  "button": "Bayar",
                  "button_app_link": "tokopedia://digital/form?category_id=52&client_number=6001799111&product_id=5108&operator_id=1816&is_from_widget=true",
                  "button_web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=1816&product_id=5108&client_number=6001799111",
                  "type": "AUTOPAY_ISSUE",
                  "is_close_button": false,
                  "is_sticky": false,
                  "tracking": [
                    {
                      "action": "click",
                      "data": "{\"event\":\"select_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 4 - 2950 - new user - 1816\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"5108\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"4\"}]}"
                    },
                    {
                      "action": "close",
                      "data": "{\"event\":\"close_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"close\",\"eventLabel\":\"todo widget - 4 - 2950 - new user - 1816\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"5108\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"4\"}]}"
                    }
                  ]
                },
                {
                  "fav_id": "438601",
                  "title": "Properti",
                  "subtitle": "Utilities + IPL",
                  "label": "5395499111",
                  "icon_url": "https://images.tokopedia.net/img/SnKlQx/2022/1/6/35d5c228-bd80-428e-8520-634f2bae6f7e.png",
                  "price": "",
                  "price_plain": 0,
                  "discount": "",
                  "slashed_price": "",
                  "reason": "Pembayaran otomatis berkendala, silakan bayar secara manual.",
                  "app_link": "tokopedia://digital/form?category_id=53&client_number=5395499111&product_id=5142&operator_id=1876&is_from_widget=true",
                  "web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=1876&product_id=5142&client_number=5395499111",
                  "option_buttons": [],
                  "button": "Bayar",
                  "button_app_link": "tokopedia://digital/form?category_id=53&client_number=5395499111&product_id=5142&operator_id=1876&is_from_widget=true",
                  "button_web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=1876&product_id=5142&client_number=5395499111",
                  "type": "AUTOPAY_ISSUE",
                  "is_close_button": false,
                  "is_sticky": false,
                  "tracking": [
                    {
                      "action": "click",
                      "data": "{\"event\":\"select_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 5 - 2950 - new user - 1876\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"5142\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"5\"}]}"
                    },
                    {
                      "action": "close",
                      "data": "{\"event\":\"close_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"close\",\"eventLabel\":\"todo widget - 5 - 2950 - new user - 1876\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"5142\",\"item_name\":\"\",\"creative_name\":\"autopay issue\",\"creative_slot\":\"5\"}]}"
                    }
                  ]
                },
                {
                  "fav_id": "376202",
                  "title": "Internet & TV Kabel",
                  "subtitle": " Indihome",
                  "label": "MANDIRI",
                  "icon_url": "https://images.tokopedia.net/img/SnKlQx/2022/1/6/eee2f222-c337-4af2-b967-839d6021dccb.png",
                  "price": "Rp102.500",
                  "price_plain": 102500,
                  "discount": "",
                  "slashed_price": "",
                  "reason": "",
                  "app_link": "tokopedia://digital/form?category_id=8&client_number=111001508111&product_id=577&operator_id=125&is_from_widget=true",
                  "web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=125&product_id=577&client_number=111001508111",
                  "option_buttons": [],
                  "button": "Aktifkan",
                  "button_app_link": "tokopedia://webview?titlebar=false&url=https%3A%2F%2Fstaging.tokopedia.com%2Fmybills%2Fadd%3Fcategory_id%3D8%26template%3Dgeneral%26menu_id%3D154%26product_id%3D577%26client_number%3D111001508111%26source%3Dshp%26operator_id%3D125%26auto_pay%3Dtrue",
                  "button_web_link": "https://staging.tokopedia.com/mybills/add?category_id=8&template=general&menu_id=154&product_id=577&client_number=111001508111&source=shp&operator_id=125&auto_pay=true",
                  "type": "AUTOPAY_ACTIVATE",
                  "is_close_button": false,
                  "is_sticky": false,
                  "tracking": [
                    {
                      "action": "click",
                      "data": "{\"event\":\"select_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 6 - 2950 - new user - 125\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"577\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"6\"}]}"
                    },
                    {
                      "action": "close",
                      "data": "{\"event\":\"close_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"close\",\"eventLabel\":\"todo widget - 6 - 2950 - new user - 125\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"577\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"6\"}]}"
                    }
                  ]
                },
                {
                  "fav_id": "25350",
                  "title": "Angsuran Kredit",
                  "subtitle": "RADANA",
                  "label": "BNI VA",
                  "icon_url": "https://images.tokopedia.net/img/SnKlQx/2022/1/6/da82e991-d96c-4ca5-b5a0-2f1ba5e81868.png",
                  "price": "Rp10.000",
                  "price_plain": 10000,
                  "discount": "",
                  "slashed_price": "",
                  "reason": "",
                  "app_link": "tokopedia://digital/form?category_id=7&client_number=191111424111&product_id=407&operator_id=88&is_from_widget=true",
                  "web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=88&product_id=407&client_number=191111424111",
                  "option_buttons": [],
                  "button": "Aktifkan",
                  "button_app_link": "tokopedia://webview?titlebar=false&url=https%3A%2F%2Fstaging.tokopedia.com%2Fmybills%2Fadd%3Fcategory_id%3D7%26template%3Dgeneral%26menu_id%3D123%26product_id%3D407%26client_number%3D191111424111%26source%3Dshp%26operator_id%3D88%26auto_pay%3Dtrue",
                  "button_web_link": "https://staging.tokopedia.com/mybills/add?category_id=7&template=general&menu_id=123&product_id=407&client_number=191111424111&source=shp&operator_id=88&auto_pay=true",
                  "type": "AUTOPAY_ACTIVATE",
                  "is_close_button": false,
                  "is_sticky": false,
                  "tracking": [
                    {
                      "action": "click",
                      "data": "{\"event\":\"select_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 7 - 2950 - new user - 88\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"407\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"7\"}]}"
                    },
                    {
                      "action": "close",
                      "data": "{\"event\":\"close_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"close\",\"eventLabel\":\"todo widget - 7 - 2950 - new user - 88\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"407\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"7\"}]}"
                    }
                  ]
                },
                {
                  "fav_id": "380048",
                  "title": "Biaya Pendidikan",
                  "subtitle": "IPMI International",
                  "label": "6001699111",
                  "icon_url": "https://images.tokopedia.net/img/SnKlQx/2022/1/6/6ad0f8e7-a750-4aa7-bc18-df1813d8236f.png",
                  "price": "",
                  "price_plain": 0,
                  "discount": "",
                  "slashed_price": "",
                  "reason": "",
                  "app_link": "tokopedia://digital/form?category_id=52&client_number=6001699111&product_id=5108&operator_id=1816&is_from_widget=true",
                  "web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=1816&product_id=5108&client_number=6001699111",
                  "option_buttons": [],
                  "button": "Aktifkan",
                  "button_app_link": "tokopedia://webview?titlebar=false&url=https%3A%2F%2Fstaging.tokopedia.com%2Fmybills%2Fadd%3Fcategory_id%3D52%26template%3Dgeneral%26menu_id%3D153%26product_id%3D5108%26client_number%3D6001699111%26source%3Dshp%26operator_id%3D1816%26auto_pay%3Dtrue",
                  "button_web_link": "https://staging.tokopedia.com/mybills/add?category_id=52&template=general&menu_id=153&product_id=5108&client_number=6001699111&source=shp&operator_id=1816&auto_pay=true",
                  "type": "AUTOPAY_ACTIVATE",
                  "is_close_button": false,
                  "is_sticky": false,
                  "tracking": [
                    {
                      "action": "click",
                      "data": "{\"event\":\"select_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 8 - 2950 - new user - 1816\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"5108\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"8\"}]}"
                    },
                    {
                      "action": "close",
                      "data": "{\"event\":\"close_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"close\",\"eventLabel\":\"todo widget - 8 - 2950 - new user - 1816\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"5108\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"8\"}]}"
                    }
                  ]
                },
                {
                  "fav_id": "377897",
                  "title": "Pajak PBB",
                  "subtitle": "2021",
                  "label": "133311111011521111",
                  "icon_url": "https://images.tokopedia.net/img/SnKlQx/2022/1/6/6b657144-bbd1-4504-9cdf-7ff63f998142.png",
                  "price": "Rp101.000",
                  "price_plain": 101000,
                  "discount": "",
                  "slashed_price": "",
                  "reason": "",
                  "app_link": "tokopedia://digital/form?category_id=22&client_number=133311111011521111&product_id=8364&operator_id=231&is_from_widget=true",
                  "web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=231&product_id=8364&client_number=133311111011521111",
                  "option_buttons": [],
                  "button": "Aktifkan",
                  "button_app_link": "tokopedia://webview?titlebar=false&url=https%3A%2F%2Fstaging.tokopedia.com%2Fmybills%2Fadd%3Fcategory_id%3D22%26template%3Dgeneral%26menu_id%3D127%26product_id%3D8364%26client_number%3D133311111011521111%26source%3Dshp%26operator_id%3D231%26auto_pay%3Dtrue",
                  "button_web_link": "https://staging.tokopedia.com/mybills/add?category_id=22&template=general&menu_id=127&product_id=8364&client_number=133311111011521111&source=shp&operator_id=231&auto_pay=true",
                  "type": "AUTOPAY_ACTIVATE",
                  "is_close_button": false,
                  "is_sticky": false,
                  "tracking": [
                    {
                      "action": "click",
                      "data": "{\"event\":\"select_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 9 - 2950 - new user - 231\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"8364\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"9\"}]}"
                    },
                    {
                      "action": "close",
                      "data": "{\"event\":\"close_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"close\",\"eventLabel\":\"todo widget - 9 - 2950 - new user - 231\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"8364\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"9\"}]}"
                    }
                  ]
                },
                {
                  "fav_id": "377794",
                  "title": "Pajak PBB",
                  "subtitle": "2021",
                  "label": "133311111013521111",
                  "icon_url": "https://images.tokopedia.net/img/SnKlQx/2022/1/6/6b657144-bbd1-4504-9cdf-7ff63f998142.png",
                  "price": "Rp301.000",
                  "price_plain": 301000,
                  "discount": "",
                  "slashed_price": "",
                  "reason": "",
                  "app_link": "tokopedia://digital/form?category_id=22&client_number=133311111013521111&product_id=8364&operator_id=231&is_from_widget=true",
                  "web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=231&product_id=8364&client_number=133311111013521111",
                  "option_buttons": [],
                  "button": "Aktifkan",
                  "button_app_link": "tokopedia://webview?titlebar=false&url=https%3A%2F%2Fstaging.tokopedia.com%2Fmybills%2Fadd%3Fcategory_id%3D22%26template%3Dgeneral%26menu_id%3D127%26product_id%3D8364%26client_number%3D133311111013521111%26source%3Dshp%26operator_id%3D231%26auto_pay%3Dtrue",
                  "button_web_link": "https://staging.tokopedia.com/mybills/add?category_id=22&template=general&menu_id=127&product_id=8364&client_number=133311111013521111&source=shp&operator_id=231&auto_pay=true",
                  "type": "AUTOPAY_ACTIVATE",
                  "is_close_button": false,
                  "is_sticky": false,
                  "tracking": [
                    {
                      "action": "click",
                      "data": "{\"event\":\"select_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 10 - 2950 - new user - 231\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"8364\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"10\"}]}"
                    },
                    {
                      "action": "close",
                      "data": "{\"event\":\"close_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"close\",\"eventLabel\":\"todo widget - 10 - 2950 - new user - 231\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"8364\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"10\"}]}"
                    }
                  ]
                },
                {
                  "fav_id": "376191",
                  "title": "Pascabayar",
                  "subtitle": "Halo Telkomsel",
                  "label": "08123123123",
                  "icon_url": "https://images.tokopedia.net/img/SnKlQx/2022/1/6/7c6be860-437b-49f0-97e1-bc1cdcd1793c.png",
                  "price": "Rp1.030",
                  "price_plain": 1030,
                  "discount": "",
                  "slashed_price": "",
                  "reason": "",
                  "app_link": "tokopedia://digital/form?category_id=9&client_number=08123123123&product_id=559&operator_id=113&is_from_widget=true",
                  "web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=113&product_id=559&client_number=08123123123",
                  "option_buttons": [],
                  "button": "Aktifkan",
                  "button_app_link": "tokopedia://webview?titlebar=false&url=https%3A%2F%2Fstaging.tokopedia.com%2Fmybills%2Fadd%3Fcategory_id%3D9%26template%3Dtelcopost%26menu_id%3D3%26product_id%3D559%26client_number%3D08123123123%26source%3Dshp%26operator_id%3D113%26auto_pay%3Dtrue",
                  "button_web_link": "https://staging.tokopedia.com/mybills/add?category_id=9&template=telcopost&menu_id=3&product_id=559&client_number=08123123123&source=shp&operator_id=113&auto_pay=true",
                  "type": "AUTOPAY_ACTIVATE",
                  "is_close_button": false,
                  "is_sticky": false,
                  "tracking": [
                    {
                      "action": "click",
                      "data": "{\"event\":\"select_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 11 - 2950 - new user - 113\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"559\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"11\"}]}"
                    },
                    {
                      "action": "close",
                      "data": "{\"event\":\"close_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"close\",\"eventLabel\":\"todo widget - 11 - 2950 - new user - 113\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"559\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"11\"}]}"
                    }
                  ]
                },
                {
                  "fav_id": "372193",
                  "title": "Air PDAM",
                  "subtitle": "AETRA JAKARTA",
                  "label": "210000006453611",
                  "icon_url": "https://images.tokopedia.net/img/SnKlQx/2022/2/7/05cab95b-f8db-4566-baf0-f65d5aa6395a.png",
                  "price": "",
                  "price_plain": 0,
                  "discount": "",
                  "slashed_price": "",
                  "reason": "",
                  "app_link": "tokopedia://digital/form?category_id=5&client_number=210000006453611&product_id=393&operator_id=29&is_from_widget=true",
                  "web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=29&product_id=393&client_number=210000006453611",
                  "option_buttons": [],
                  "button": "Aktifkan",
                  "button_app_link": "tokopedia://webview?titlebar=false&url=https%3A%2F%2Fstaging.tokopedia.com%2Fmybills%2Fadd%3Fcategory_id%3D5%26template%3Dgeneral%26menu_id%3D120%26product_id%3D393%26client_number%3D210000006453611%26source%3Dshp%26operator_id%3D29%26auto_pay%3Dtrue",
                  "button_web_link": "https://staging.tokopedia.com/mybills/add?category_id=5&template=general&menu_id=120&product_id=393&client_number=210000006453611&source=shp&operator_id=29&auto_pay=true",
                  "type": "AUTOPAY_ACTIVATE",
                  "is_close_button": false,
                  "is_sticky": false,
                  "tracking": [
                    {
                      "action": "click",
                      "data": "{\"event\":\"select_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 12 - 2950 - new user - 29\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"393\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"12\"}]}"
                    },
                    {
                      "action": "close",
                      "data": "{\"event\":\"close_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"close\",\"eventLabel\":\"todo widget - 12 - 2950 - new user - 29\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"393\",\"item_name\":\"\",\"creative_name\":\"autopay activate\",\"creative_slot\":\"12\"}]}"
                    }
                  ]
                }
              ],
              "items_id": 1,
              "object_id": "",
              "title": "Perlu dicek di Bayar Otomatis kamu 👇",
              "content": "",
              "sub_title": "",
              "web_link": "",
              "text_link": "",
              "media_url": "",
              "media_url_desktop": "",
              "template": "",
              "button_type": "",
              "server_date": "",
              "due_date": "",
              "label_1": "",
              "label_2": "",
              "label_3": "",
              "attributes": {
                "title_color": "",
                "sub_title_color": "",
                "media_url_title": "",
                "media_url_type": "",
                "icon_url": "",
                "sold_value": "",
                "sold_percentage_value": 0,
                "sold_percentage_label": "",
                "sold_percentage_label_color": "",
                "show_sold_percentage": false,
                "campaign_label_text": "",
                "campaign_label_text_color": "",
                "campaign_label_background_url": "",
                "rating_type": "",
                "rating": 0,
                "review": "",
                "special_info_text": "",
                "special_info_color": "",
                "special_discount": "",
                "cashback": "",
                "price_prefix": "",
                "price_suffix": "",
                "tag_icon": "",
                "tag_label": "",
                "is_grayscale": false,
                "__typename": "RechargeDynamicPageItemAttributes"
              },
              "__typename": "RechargeDynamicPageItem"
            },
            {
              "tracking": [],
              "widgets": [
                {
                  "fav_id": "0",
                  "title": "Bayar Sekaligus",
                  "subtitle": "1+ tagihan",
                  "label": "",
                  "icon_url": "https://images.tokopedia.net/img/View%20All%20Card%20Alternate@3x.png",
                  "price": "",
                  "price_plain": 0,
                  "discount": "",
                  "slashed_price": "",
                  "reason": "",
                  "app_link": "tokopedia://webview?titlebar=false&url=https://staging.tokopedia.com/mybills/?show%3Dbills",
                  "web_link": "https://staging.tokopedia.com/mybills/?show=bills",
                  "option_buttons": [],
                  "button": "",
                  "button_app_link": "",
                  "button_web_link": "",
                  "type": "BAYAR_SEKALIGUS",
                  "is_close_button": false,
                  "is_sticky": true,
                  "tracking": [
                    {
                      "action": "click",
                      "data": "{\"event\":\"select_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 0 - 2950 - new user\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"\",\"item_name\":\"\",\"creative_name\":\"\",\"creative_slot\":\"0\"}]}"
                    }
                  ]
                },
                {
                  "fav_id": "376202",
                  "title": "Internet & TV Kabel",
                  "subtitle": "Indihome",
                  "label": "MANDIRI",
                  "icon_url": "https://images.tokopedia.net/img/SnKlQx/2022/1/6/eee2f222-c337-4af2-b967-839d6021dccb.png",
                  "price": "Rp102.500",
                  "price_plain": 0,
                  "discount": "",
                  "slashed_price": "",
                  "reason": "",
                  "app_link": "tokopedia://digital/form?category_id=8&client_number=111001508111&product_id=577&operator_id=125&is_from_widget=true",
                  "web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=125&product_id=577&client_number=111001508111",
                  "option_buttons": [],
                  "button": "",
                  "button_app_link": "",
                  "button_web_link": "",
                  "type": "POSTPAID_REMINDER",
                  "is_close_button": false,
                  "is_sticky": false,
                  "tracking": [
                    {
                      "action": "click",
                      "data": "{\"event\":\"select_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 1 - 2950 - new user - Internet \\u0026 TV Kabel - 125\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"577\",\"item_name\":\"\",\"creative_name\":\"todo reminder postpaid\",\"creative_slot\":\"1\"}]}"
                    }
                  ]
                },
                {
                  "fav_id": "25350",
                  "title": "Angsuran Kredit",
                  "subtitle": "Radana Finance",
                  "label": "BNI VA",
                  "icon_url": "https://images.tokopedia.net/img/SnKlQx/2022/1/6/da82e991-d96c-4ca5-b5a0-2f1ba5e81868.png",
                  "price": "Rp10.000",
                  "price_plain": 0,
                  "discount": "",
                  "slashed_price": "",
                  "reason": "",
                  "app_link": "tokopedia://digital/form?category_id=7&client_number=191111424111&product_id=407&operator_id=88&is_from_widget=true",
                  "web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=88&product_id=407&client_number=191111424111",
                  "option_buttons": [],
                  "button": "",
                  "button_app_link": "",
                  "button_web_link": "",
                  "type": "POSTPAID_REMINDER",
                  "is_close_button": false,
                  "is_sticky": false,
                  "tracking": [
                    {
                      "action": "click",
                      "data": "{\"event\":\"select_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 2 - 2950 - new user - Angsuran Kredit - 88\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"407\",\"item_name\":\"\",\"creative_name\":\"todo reminder postpaid\",\"creative_slot\":\"2\"}]}"
                    }
                  ]
                },
                {
                  "fav_id": "377897",
                  "title": "Pajak PBB",
                  "subtitle": "PBB DKI Jakarta",
                  "label": "133311111011521111",
                  "icon_url": "https://images.tokopedia.net/img/SnKlQx/2022/1/6/6b657144-bbd1-4504-9cdf-7ff63f998142.png",
                  "price": "Rp101.000",
                  "price_plain": 0,
                  "discount": "",
                  "slashed_price": "",
                  "reason": "",
                  "app_link": "tokopedia://digital/form?category_id=22&client_number=133311111011521111&product_id=8364&operator_id=231&is_from_widget=true",
                  "web_link": "https://pulsa-staging.tokopedia.com/?action=init_data&operator_id=231&product_id=8364&client_number=133311111011521111",
                  "option_buttons": [],
                  "button": "",
                  "button_app_link": "",
                  "button_web_link": "",
                  "type": "POSTPAID_REMINDER",
                  "is_close_button": false,
                  "is_sticky": false,
                  "tracking": [
                    {
                      "action": "click",
                      "data": "{\"event\":\"select_content\",\"eventCategory\":\"digital - subhomepage\",\"eventAction\":\"click\",\"eventLabel\":\"todo widget - 3 - 2950 - new user - Pajak PBB - 231\",\"screenName\":\"/top-up-tagihan\",\"currentSite\":\"tokopediadigitalRecharge\",\"businessUnit\":\"recharge\",\"userId\":\"9104453\",\"promotions\":[{\"item_id\":\"8364\",\"item_name\":\"\",\"creative_name\":\"todo reminder postpaid\",\"creative_slot\":\"3\"}]}"
                    }
                  ]
                }
              ],
              "items_id": 2,
              "object_id": "",
              "title": "Saatnya bayar dan beli ini",
              "content": "",
              "sub_title": "",
              "web_link": "",
              "text_link": "",
              "media_url": "",
              "media_url_desktop": "",
              "template": "",
              "button_type": "",
              "server_date": "",
              "due_date": "",
              "label_1": "",
              "label_2": "",
              "label_3": "",
              "attributes": {
                "title_color": "",
                "sub_title_color": "",
                "media_url_title": "",
                "media_url_type": "",
                "icon_url": "",
                "sold_value": "",
                "sold_percentage_value": 0,
                "sold_percentage_label": "",
                "sold_percentage_label_color": "",
                "show_sold_percentage": false,
                "campaign_label_text": "",
                "campaign_label_text_color": "",
                "campaign_label_background_url": "",
                "rating_type": "",
                "rating": 0,
                "review": "",
                "special_info_text": "",
                "special_info_color": "",
                "special_discount": "",
                "cashback": "",
                "price_prefix": "",
                "price_suffix": "",
                "tag_icon": "",
                "tag_label": "",
                "is_grayscale": false,
                "__typename": "RechargeDynamicPageItemAttributes"
              },
              "__typename": "RechargeDynamicPageItem"
            }
          ],
          "__typename": "RechargeDynamicPageSection"
        }
      ],
      "__typename": "RechargeDynamicPage"
    }
  }
        """
    }
}
