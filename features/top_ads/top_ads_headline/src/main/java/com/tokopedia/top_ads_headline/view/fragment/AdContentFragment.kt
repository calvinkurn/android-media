package com.tokopedia.top_ads_headline.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.CpmModelMapper
import com.tokopedia.top_ads_headline.data.CreateHeadlineAdsStepperModel
import com.tokopedia.top_ads_headline.data.TopAdsCategoryDataModel
import com.tokopedia.top_ads_headline.data.TopAdsManageHeadlineInput
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.activity.HeadlineStepperActivity
import com.tokopedia.top_ads_headline.view.activity.IS_EDITED
import com.tokopedia.top_ads_headline.view.activity.SELECTED_PRODUCT_LIST
import com.tokopedia.top_ads_headline.view.activity.TopAdsProductListActivity
import com.tokopedia.top_ads_headline.view.sheet.PromotionalMessageBottomSheet
import com.tokopedia.top_ads_headline.view.viewmodel.AdContentViewModel
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.common.view.TopAdsProductImagePreviewWidget
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.ad_content_fragment.*
import javax.inject.Inject

private const val SELECT_PRODUCT_REQUEST_CODE = 1001
private const val MAX_PRODUCT_PREVIEW = 3
private const val MIN_PROMOTIONAL_MSG_COUNT = 20

class AdContentFragment : BaseHeadlineStepperFragment<CreateHeadlineAdsStepperModel>(), TopAdsProductImagePreviewWidget.TopAdsImagePreviewClick {

    private var selectedTopAdsProducts = ArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>()

    companion object {
        fun newInstance() = AdContentFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var cpmModelMapper: CpmModelMapper

    private lateinit var viewModel: AdContentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AdContentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ad_content_fragment, container, false)
    }

    override fun getScreenName(): String {
        return AdContentFragment::class.java.simpleName
    }

    override fun initInjector() {
        DaggerHeadlineAdsComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }

    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateHeadlineAdsStepperModel()
    }

    override fun gotoNextPage() {
        stepperModel?.slogan = promotionalMessageInputText.textFieldInput.text.toString()
        stepperModel?.selectedProductIds = getAdItems()
        stepperModel?.adOperations = getAdOperations()
        stepperListener?.goToNextPage(stepperModel)
    }

    private fun getAdOperations(): MutableList<TopAdsManageHeadlineInput.Operation.Group.AdOperation> {
        return mutableListOf(TopAdsManageHeadlineInput.Operation.Group.AdOperation(
                action = ParamObject.ACTION_CREATE,
                ad = TopAdsManageHeadlineInput.Operation.Group.AdOperation.Ad(
                        id = "0",
                        title = stepperModel?.groupName ?: "",
                        slogan = stepperModel?.slogan
                                ?: "",
                        productIDs = ArrayList<String>().apply {
                            stepperModel?.selectedProductIds?.forEach {
                                add(it.toString())
                            }
                        }
                )
        ))
    }

    private fun getAdItems(): MutableList<Int> {
        return selectedTopAdsProducts.map {
            it.productID
        }.toMutableList()
    }

    override fun updateToolBar() {
        if (activity is HeadlineStepperActivity) {
            (activity as HeadlineStepperActivity).updateToolbarTitle(getString(R.string.topads_headline_ad_content_fragment_label))
        }
    }

    override fun populateView() {
        setUpSelectedText()
        productImagePreviewWidget.setTopAdsImagePreviewClick(this)
        if (selectedTopAdsProducts.isNotEmpty()) {
            onProductsSelectionChange()
        }
        stepperModel?.slogan = getString(R.string.topads_headline_promotional_dummy_message)
        promotionalMessageInputText.textFieldInput.setText(getString(R.string.topads_headline_promotional_dummy_message))
        promotionalMessageInputText.textFieldInput.isFocusable = false
        promotionalMessageInputText.textFieldInput.setOnClickListener {
            openPromotionalMessageBottomSheet()
        }
        btnSubmit.setOnClickListener {
            onClickSubmit()
        }
        fetchProductsForPreview()
    }

    private fun fetchProductsForPreview() {
        if (selectedTopAdsProducts.isEmpty()) {
            viewModel.getTopAdsProductList(userSession.shopId.toIntOrZero(), "", "", "", "", MAX_PRODUCT_PREVIEW, 0, this::showTopAdsBannerPreview, this::onError)
        }
    }

    private fun onClickSubmit() {
        when {
            selectedTopAdsProducts.isEmpty() -> {
                productPickerErrorText.show()
                view?.let { it1 ->
                    Toaster.toasterCustomBottomHeight = resources.getDimensionPixelSize(R.dimen.dp_60)
                    Toaster.build(it1, getString(R.string.topads_headline_submit_ad_detail_error), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                }
            }
            else -> {
                gotoNextPage()
            }
        }
    }

    private fun showTopAdsBannerPreview(responseProductList: List<ResponseProductList.Result.TopadsGetListProduct.Data> = emptyList()) {
        val cpmModel = if (selectedTopAdsProducts.isNotEmpty()) {
            cpmModelMapper.getCpmModelResponse(selectedTopAdsProducts.take(MAX_PRODUCT_PREVIEW), stepperModel?.slogan
                    ?: "")
        } else {
            cpmModelMapper.getCpmModelResponse(responseProductList.take(MAX_PRODUCT_PREVIEW), stepperModel?.slogan
                    ?: "")
        }
        stepperModel?.cpmModel = cpmModel
        topAdsBannerView.displayAds(cpmModel)
    }

    private fun onError(message: String) {
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    private fun openPromotionalMessageBottomSheet() {
        val promotionalMessageBottomSheet = PromotionalMessageBottomSheet.newInstance(userSession.shopName,
                this::onPromotionalBottomSheetDismiss)
        promotionalMessageBottomSheet.show(childFragmentManager, "")
    }

    private fun onPromotionalBottomSheetDismiss(promotionalMessage: String) {
        promotionalMessageInputText.textFieldInput.setText(promotionalMessage)
        stepperModel?.let {
            it.slogan = promotionalMessage
            showTopAdsBannerPreview()
        }
        btnSubmit.isEnabled = promotionalMessage.length >= MIN_PROMOTIONAL_MSG_COUNT
    }

    private fun setUpSelectedText() {
        contentSelectedText.text = getString(R.string.topads_headline_product_selected, selectedTopAdsProducts.size.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PRODUCT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            stepperModel?.selectedTopAdsProductMap = data?.getSerializableExtra(SELECTED_PRODUCT_LIST) as? HashMap<TopAdsCategoryDataModel, ArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>>
                    ?: HashMap()
            selectedTopAdsProducts = getSelectedProducts()
            if (data?.getBooleanExtra(IS_EDITED, false) == true) {
                onProductsSelectionChange()
            }
            if (selectedTopAdsProducts.isNotEmpty()) {
                productPickerErrorText.hide()
            }
        }
    }

    private fun onProductsSelectionChange() {
        val imageList = ArrayList<String>()
        selectedTopAdsProducts.forEach {
            imageList.add(it.productImage)
        }
        if (selectedTopAdsProducts.size >= MAX_PRODUCT_PREVIEW - 1) {
            showTopAdsBannerPreview()
        }
        productImagePreviewWidget.setSelectedProductList(imageList)
        setUpSelectedText()
    }

    override fun onDeletePreview(position: Int) {
        val product = selectedTopAdsProducts[position]
        stepperModel?.selectedTopAdsProductMap?.forEach { (_, arrayList) ->
            if (arrayList.contains(product)) {
                arrayList.remove(product)
            }
        }
        selectedTopAdsProducts.removeAt(position)
        setUpSelectedText()
    }

    override fun onClickPreview(position: Int) {
        if (position == 0) {
            val intent = Intent(activity, TopAdsProductListActivity::class.java)
            intent.putExtra(SELECTED_PRODUCT_LIST, stepperModel?.selectedTopAdsProductMap)
            startActivityForResult(intent, SELECT_PRODUCT_REQUEST_CODE)
        }
    }

    private fun getSelectedProducts(): ArrayList<ResponseProductList.Result.TopadsGetListProduct.Data> {
        val result = HashSet<ResponseProductList.Result.TopadsGetListProduct.Data>()
        stepperModel?.selectedTopAdsProductMap?.forEach { (_, value) ->
            if (value.size > 1) {
                result.addAll(value)
            }
        }
        return result.toCollection(ArrayList())
    }
}