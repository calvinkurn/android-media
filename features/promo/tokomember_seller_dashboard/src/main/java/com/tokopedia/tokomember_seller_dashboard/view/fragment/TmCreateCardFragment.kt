package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.tokomember_common_widget.TokomemberShopView
import com.tokopedia.tokomember_common_widget.model.TokomemberShopCardModel
import com.tokopedia.tokomember_common_widget.util.CreateScreenType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmOpenFragmentCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.Card
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.CardTemplate
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.IntoolsShop
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCardModifyInput
import com.tokopedia.tokomember_seller_dashboard.model.CardDataTemplate
import com.tokopedia.tokomember_seller_dashboard.model.CardTemplateImageListItem
import com.tokopedia.tokomember_seller_dashboard.model.TmIntroBottomsheetModel
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CARD_DATA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CARD_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_TYPE
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_AVATAR
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_NAME
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_CTA
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_CTA_RETRY
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_DESC
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_TITLE_RETRY
import com.tokopedia.tokomember_seller_dashboard.util.LOADING_TEXT
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashIntroActivity
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberCardBgAdapter
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberCardBgAdapterListener
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberCardColorAdapter
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberCardColorAdapterListener
import com.tokopedia.tokomember_seller_dashboard.view.adapter.decoration.TokomemberDashColorItemDecoration
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TokomemberCardBgFactory
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TokomemberCardColorFactory
import com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper.TokomemberCardMapper
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardBg
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardBgItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardColor
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardColorItem
import com.tokopedia.tokomember_seller_dashboard.view.customview.BottomSheetClickListener
import com.tokopedia.tokomember_seller_dashboard.view.customview.TokomemberBottomsheet
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmDashCreateViewModel
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.tm_dash_create_card.*
import kotlinx.android.synthetic.main.tm_dash_create_card_container.*
import javax.inject.Inject

class TmCreateCardFragment : BaseDaggerFragment(), TokomemberCardColorAdapterListener,
    TokomemberCardBgAdapterListener, BottomSheetClickListener {

    private lateinit var tmOpenFragmentCallback: TmOpenFragmentCallback
    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private var isColorPalleteClicked = false
    private var mCardBgTemplateList = arrayListOf<CardTemplateImageListItem>()
    var shopViewPremium: TokomemberShopView? = null
    var shopViewVip: TokomemberShopView? = null
    private var shopID = 0
    private var retryCount = 0
    private var tmShopCardModel = TokomemberShopCardModel()
    private var mTmCardModifyInput = TmCardModifyInput()
    private var loaderDialog: LoaderDialog?=null
    private val tmDashCreateViewModel: TmDashCreateViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TmDashCreateViewModel::class.java)
    }
    private val adapterBg: TokomemberCardBgAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TokomemberCardBgAdapter(arrayListOf(), TokomemberCardBgFactory(this))
    }

    private val adapterColor: TokomemberCardColorAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TokomemberCardColorAdapter(arrayListOf(), TokomemberCardColorFactory(this))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TmOpenFragmentCallback) {
            tmOpenFragmentCallback =  context as TmOpenFragmentCallback
        } else {
            throw Exception(context.toString() )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_create_card_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        tmDashCreateViewModel.getCardInfo(arguments?.getInt(BUNDLE_CARD_ID)?:0)
        renderHeader()
        tipTokomember.setOnClickListener {
            Toast.makeText(context, "Click tips", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    private fun observeViewModel() {
        tmDashCreateViewModel.tmCardResultLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                 TokoLiveDataResult.STATUS.LOADING -> {
                     containerViewFlipper.displayedChild = SHIMMER
                 }
                 TokoLiveDataResult.STATUS.SUCCESS -> {
                    mCardBgTemplateList.addAll(it.data?.cardTemplateImageList as ArrayList<CardTemplateImageListItem>)
                    renderCardUi(it.data)
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    handleErrorUiOnErrorData()
                }
            }
        })

        tmDashCreateViewModel.tokomemberCardBgResultLiveData.observe(
            viewLifecycleOwner,
            {
                when (it) {
                    is Success -> {
                        renderBgTemplateList(it.data)
                    }
                    is Fail -> {
                        handleErrorUiOnErrorData()
                    }
                }
            })

        tmDashCreateViewModel.tokomemberCardColorResultLiveData.observe(
            viewLifecycleOwner,
            {
                when (it) {
                    is Success -> {
                        renderColorTemplateList(it.data)
                    }
                    is Fail -> {
                        handleErrorUiOnErrorData()
                    }
                }
            })

        tmDashCreateViewModel.tokomemberCardModifyLiveData.observe(viewLifecycleOwner,{
            viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.DESTROYED)
            when(it.status) {
                TokoLiveDataResult.STATUS.LOADING -> {
                    openLoadingDialog()
                }
                 TokoLiveDataResult.STATUS.SUCCESS -> {
                     closeLoadingDialog()
                     openProgramCreationPage()
                }
                 TokoLiveDataResult.STATUS.ERROR -> {
                     closeLoadingDialog()
                     handleErrorUiOnUpdate()
                }
            }
        })
    }

    private fun handleErrorUiOnErrorData(){
        containerViewFlipper.displayedChild = ERROR
        globalError.setActionClickListener {
            tmDashCreateViewModel.getCardInfo(arguments?.getInt("cardID")?:0)
        }
    }

    private fun handleErrorUiOnUpdate(){
        val title = when(retryCount){
            0-> ERROR_CREATING_TITLE
            else -> ERROR_CREATING_TITLE_RETRY
        }
        val cta = when(retryCount){
            0-> ERROR_CREATING_CTA
            else -> ERROR_CREATING_CTA_RETRY
        }
        val bundle = Bundle()
        //TODO use remote res
        val tmIntroBottomSheetModel = TmIntroBottomsheetModel(title, ERROR_CREATING_DESC , "https://images.tokopedia.net/img/android/res/singleDpi/quest_widget_nonlogin_banner.png", cta , errorCount = retryCount)
        bundle.putString(TokomemberBottomsheet.ARG_BOTTOMSHEET, Gson().toJson(tmIntroBottomSheetModel))
        val bottomSheet = TokomemberBottomsheet.createInstance(bundle)
        bottomSheet.show(childFragmentManager,"")
        retryCount +=1
    }

    private fun openLoadingDialog(){

        loaderDialog = context?.let { LoaderDialog(it) }
        loaderDialog?.loaderText?.apply {
            setType(Typography.DISPLAY_2)
        }
        loaderDialog?.setLoadingText(Html.fromHtml(LOADING_TEXT))
        loaderDialog?.show()
    }

    private fun closeLoadingDialog(){
        loaderDialog?.dialog?.dismiss()
    }

    private fun openProgramCreationPage() {
        val bundle = Bundle()
        bundle.putInt(BUNDLE_PROGRAM_TYPE, 0)
        bundle.putInt(BUNDLE_SHOP_ID, shopID)
        bundle.putInt(BUNDLE_CARD_ID,arguments?.getInt(BUNDLE_CARD_ID)?:0)
        bundle.putString(BUNDLE_SHOP_AVATAR, arguments?.getString(BUNDLE_SHOP_AVATAR))
        bundle.putString(BUNDLE_SHOP_NAME, arguments?.getString(BUNDLE_SHOP_NAME))
        bundle.putParcelable(BUNDLE_CARD_DATA , tmShopCardModel)
        tmOpenFragmentCallback.openFragment(CreateScreenType.PROGRAM, bundle)
    }

    private fun renderCardUi(data: CardDataTemplate) {
        containerViewFlipper.displayedChild = DATA
        shopID = data.card?.shopID?:0
        renderCardCarousel(data)
        btnContinueCard?.setOnClickListener {
            mTmCardModifyInput =  TmCardModifyInput(
                apiVersion = "3.0.0",
                isMerchantCard = true,
                intoolsShop = IntoolsShop(id = data.card?.shopID),
                cardTemplate = CardTemplate(
                    fontColor = "#FFFFFF",
                    backgroundImgUrl = shopViewPremium?.getCardBackgroundImageUrl()
                ),
                card = Card(
                    shopID = data.card?.shopID,
                    name = shopViewPremium?.getCardShopName(),
                    numberOfLevel = 2
                )
            )
            tmDashCreateViewModel.modifyShopCard(mTmCardModifyInput)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderBgTemplateList(data: TokomemberCardBgItem) {
        (rvCardBg.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        val layoutManagerBg = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvCardBg.layoutManager = layoutManagerBg
        rvCardBg.adapter = adapterBg
        if (rvCardBg.itemDecorationCount.isZero()) {
            rvCardBg.addItemDecoration(TokomemberDashColorItemDecoration(dpToPx(12).toInt()))
        }
        adapterBg.addItems(data = data.tokoVisitableCardBg)
        adapterBg.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderColorTemplateList(data: TokomemberCardColorItem) {
        (rvColor.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvColor.layoutManager = layoutManager
        rvColor.adapter = adapterColor
        if (rvColor.itemDecorationCount.isZero()) {
            rvColor.addItemDecoration(TokomemberDashColorItemDecoration(dpToPx(12).toInt()))
        }
        adapterColor.addItems(data = data.tokoVisitableCardColor)
        adapterColor.notifyDataSetChanged()
    }

    private fun renderHeader() {
        headerCard?.apply {
            title = HEADER_TITLE
            subtitle = HEADER_DESC
            isShowBackButton = true
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
        progressCard?.apply {
            progressBarColorType = ProgressBarUnify.COLOR_GREEN
            progressBarHeight = ProgressBarUnify.SIZE_SMALL
            setValue(25, false)
        }
    }

    private fun renderCardCarousel(data: CardDataTemplate) {
        context?.let {
            shopViewPremium = TokomemberShopView(it)
            shopViewVip = TokomemberShopView(it)
            tmShopCardModel = TokomemberShopCardModel(
                shopName = data.card?.name ?: "",
                numberOfLevel = data.card?.numberOfLevel ?: 0,
                backgroundColor = data.cardTemplate?.backgroundColor ?: "",
                backgroundImgUrl = data.cardTemplate?.backgroundImgUrl ?: "",
                shopType = 0,
                shopIconUrl = data.shopAvatar
            )
            shopViewPremium?.apply {
                setShopCardData(tmShopCardModel)
            }
            shopViewVip?.apply {
                setShopCardData(tmShopCardModel.apply {
                    shopType = 1
                })
            }

            carouselCard.apply {
                indicatorPosition = CarouselUnify.INDICATOR_BC
                slideToShow = 1f
                slideToScroll = 1
                freeMode = false
                centerMode = true
                autoplay = false
                addItem(shopViewPremium?:TokomemberShopView(context))
                addItem(shopViewVip?:TokomemberShopView(context))
                onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
                    override fun onActiveIndexChanged(prev: Int, current: Int) {

                    }
                }
                onDragEventListener = object : CarouselUnify.OnDragEventListener {
                    override fun onDrag(progress: Float) {

                    }
                }
            }
        }
    }

    override fun onItemDisplayedCardBg(tokoCardItem: Visitable<*>, position: Int) {

    }

    override fun onItemDisplayedCardColor(tokoCardItem: Visitable<*>, position: Int) {

    }

    override fun onItemClickCardCBg(tokoCardItem: Visitable<*>, position: Int) {
        if (isColorPalleteClicked && position != 1) {
            adapterBg.notifyItemChanged(position)
            if (tokoCardItem is TokomemberCardBg) {

                tmShopCardModel = TokomemberShopCardModel(
                    shopName = tmShopCardModel.shopName,
                    numberOfLevel = tmShopCardModel.numberOfLevel,
                    backgroundColor = tmShopCardModel.backgroundColor,
                    backgroundImgUrl = tokoCardItem.imageUrl ?: "",
                    shopType = 0
                )
                shopViewPremium?.setShopCardData(tmShopCardModel)
                shopViewVip?.setShopCardData(tmShopCardModel.apply {
                    shopType = 1
                })
            }
        }
    }

    override fun onItemClickCardColorSelect(tokoCardItem: Visitable<*>?, position: Int) {
        if (position != -1) {
            adapterColor.notifyItemChanged(position)
            isColorPalleteClicked = true
            if (tokoCardItem is TokomemberCardColor) {
                getBackgroundPallete(tokoCardItem.id ?: "", tokoCardItem.tokoCardPatternList)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getBackgroundPallete(colorCode: String, arrayList: ArrayList<String>) {
        val bgItem = TokomemberCardMapper.getBackground(mCardBgTemplateList, colorCode, arrayList)
        adapterBg.addItems(bgItem.tokoVisitableCardBg)
        adapterBg.notifyDataSetChanged()
    }

    override fun onItemClickCardColorUnselect(tokoCardItem: Visitable<*>?, position: Int) {
        rvColor?.post {
            adapterColor.unselectModel(position)
        }
    }

    override fun onStop() {
        super.onStop()
        tmDashCreateViewModel.tokomemberCardModifyLiveData.removeObservers(this)
    }

    override fun onButtonClick(errorCount: Int) {
        when(errorCount){
            0 -> tmDashCreateViewModel.modifyShopCard(mTmCardModifyInput)
            else -> {
                (TokomemberDashIntroActivity.openActivity(
                    shopID, arguments?.getString(BUNDLE_SHOP_AVATAR)?:"",
                        arguments?.getString(BUNDLE_SHOP_NAME)?:"",
                    false,
                    this.context
                ))
            }
        }
    }

    companion object {
        const val TAG_CARD_CREATE = "CARD_CREATE"
        const val HEADER_TITLE = "Daftar TokoMember"
        const val HEADER_DESC = "Langkah 1 dari 4 "
        const val DATA = 1
        const val SHIMMER = 0
        const val ERROR = 2
        fun newInstance(bundle: Bundle): TmCreateCardFragment {
            return TmCreateCardFragment().apply {
                arguments = bundle
            }
        }
    }
}