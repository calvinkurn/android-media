package com.tokopedia.buyerorder.recharge.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.recharge.presentation.model.*
import com.tokopedia.digital.digital_recommendation.domain.DigitalRecommendationUseCase
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl

/**
 * @author by furqan on 01/11/2021
 */
class RechargeOrderDetailAdapter(typeFactory: RechargeOrderDetailTypeFactory) :
        BaseAdapter<RechargeOrderDetailTypeFactory>(typeFactory) {

    fun updateItems(data: RechargeOrderDetailModel?,
                    topAdsData: BestSellerDataModel?,
                    recommendationWidgetPosition: List<String>?) {
        data?.let {
            val newItems = setupItems(it, topAdsData, recommendationWidgetPosition)
            visitables.clear()
            visitables.addAll(newItems)
            notifyDataSetChanged()
        }
    }

    private fun setupItems(
            data: RechargeOrderDetailModel,
            topAdsData: BestSellerDataModel?,
            recommendationWidgetPosition: List<String>?
    ): List<Visitable<*>> {
        return mutableListOf<Visitable<*>>().apply {
            setupTopSection(data.topSectionModel)
            setupDetailSection(data.detailsSection)
            setupPaymentSection(data.paymentSectionModel)
            recommendationWidgetPosition?.let {
                for (title in it) {
                    if (title == DigitalRecommendationUseCase.DG_RECOM_NAME)
                        setupDigitalRecommendationWidget()
                    else if (title == DigitalRecommendationUseCase.PG_RECOM_NAME)
                        topAdsData?.let { topAds ->
                            setupTopAdsWidget(topAds)
                        }
                }
            }
            setupSBMStaticButton()
            setupLanggananStaticButton()
            setupAboutOrdersSection(data.helpUrl)
            setupActionButtonSection(data.actionButtonList)
        }
    }

    private fun MutableList<Visitable<*>>.setupTopSection(topModel: RechargeOrderDetailTopSectionModel) {
        add(topModel)
        addDivider()
    }

    private fun MutableList<Visitable<*>>.setupDetailSection(detailModel: RechargeOrderDetailSectionModel) {
        add(detailModel)
        addDivider()
    }

    private fun MutableList<Visitable<*>>.setupPaymentSection(paymentModel: RechargeOrderDetailPaymentModel) {
        add(paymentModel)
        addDivider()
    }

    private fun MutableList<Visitable<*>>.setupDigitalRecommendationWidget() {
        add(RechargeOrderDetailDigitalRecommendationModel())
        addDivider()
    }

    private fun MutableList<Visitable<*>>.setupTopAdsWidget(topAdsData: BestSellerDataModel) {
        add(topAdsData)
        addDivider()
    }

    private fun MutableList<Visitable<*>>.setupSBMStaticButton() {
        add(RechargeOrderDetailStaticButtonModel(
                buttonType= StaticButtonType.SBM,
                iconUrl = "",
                title = "",
                titleRes = R.string.recharge_order_detail_sbm_label,
                subtitle = "",
                subtitleRes = R.string.recharge_order_detail_sbm_detail,
                actionUrl = getBayarSekaligusUrl()
        ))
        addDivider()
    }

    private fun MutableList<Visitable<*>>.setupLanggananStaticButton() {
        add(RechargeOrderDetailStaticButtonModel(
                buttonType= StaticButtonType.LANGGANAN,
                iconUrl = "",
                title = "",
                titleRes = R.string.recharge_order_detail_mybills_label,
                subtitle = "",
                subtitleRes = R.string.recharge_order_detail_mybills_detail,
                actionUrl = getLanggananUrl()
        ))
        addDivider()
    }

    private fun MutableList<Visitable<*>>.setupAboutOrdersSection(helpUrl: String) {
        add(RechargeOrderDetailAboutOrderModel(helpUrl))
    }

    private fun MutableList<Visitable<*>>.setupActionButtonSection(actionButtonList: RechargeOrderDetailActionButtonListModel) {
        add(actionButtonList)
    }

    private fun MutableList<Visitable<*>>.addDivider() {
        add(RechargeOrderDetailDividerModel())
    }

    fun removeDigitalRecommendation() {
        val index = visitables.indexOfLast { it is RechargeOrderDetailDigitalRecommendationModel }

        if (index != -1) {
            visitables.removeAt(index + 1) // remove divider
            notifyItemRemoved(index + 1)
            visitables.removeAt(index) // remove digital recommendation
            notifyItemRemoved(index)
        }
    }

    fun removeTopAds() {
        val index = visitables.indexOfLast { it is BestSellerDataModel }

        if (index != -1) {
            visitables.removeAt(index + 1) // remove divider
            notifyItemRemoved(index + 1)
            visitables.removeAt(index) // remove topads
            notifyItemRemoved(index)
        }
    }

    fun lastVisibleIsActionButton(lastVisibleItemPosition: Int): Boolean =
            if (visitables.size > lastVisibleItemPosition) {
                visitables[lastVisibleItemPosition] is RechargeOrderDetailActionButtonListModel
            } else {
                false
            }

    private fun getLanggananUrl(): String {
        return if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            ACTION_URL_LANGGANAN_STAGING
        } else {
            ACTION_URL_LANGGANAN
        }
    }

    private fun getBayarSekaligusUrl(): String {
        return if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            ACTION_URL_BAYAR_SEKALIGUS_STAGING
        } else {
            ACTION_URL_BAYAR_SEKALIGUS
        }
    }

    companion object {
        private const val ACTION_URL_LANGGANAN =
            "tokopedia://webview?titlebar=false&url=https://www.tokopedia.com/mybills/?show=subscription"
        private const val ACTION_URL_LANGGANAN_STAGING =
            "tokopedia://webview?titlebar=false&url=https://staging.tokopedia.com/mybills/?show=subscription"
        private const val ACTION_URL_BAYAR_SEKALIGUS =
            "tokopedia://webview?titlebar=false&url=https://www.tokopedia.com/mybills/?show=bills"
        private const val ACTION_URL_BAYAR_SEKALIGUS_STAGING =
            "tokopedia://webview?titlebar=false&url=https://staging.tokopedia.com/mybills/?show=bills"
    }
}
