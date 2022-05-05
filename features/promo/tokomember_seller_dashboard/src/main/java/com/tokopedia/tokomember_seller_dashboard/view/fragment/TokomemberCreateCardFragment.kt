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
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.tokomember_common_widget.TokomemberShopView
import com.tokopedia.tokomember_common_widget.model.TokomemberShopCardModel
import com.tokopedia.tokomember_common_widget.util.ProgramScreenType
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
import com.tokopedia.tokomember_seller_dashboard.util.*
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
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashCreateViewModel
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.tm_dash_create_card.*
import javax.inject.Inject

class TokomemberCreateCardFragment : BaseDaggerFragment(), TokomemberCardColorAdapterListener,
    TokomemberCardBgAdapterListener  , BottomSheetClickListener {

    private lateinit var tmOpenFragmentCallback: TmOpenFragmentCallback
    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private var isColorPalleteClicked = false
    private var mCardBgTemplateList = arrayListOf<CardTemplateImageListItem>()
    var shopViewPremium: TokomemberShopView? = null
    var shopViewVip: TokomemberShopView? = null
    private var shopID = 0
    private var retryCount = 0
    private var tokomemberShopCardModel = TokomemberShopCardModel()
    private var mTmCardModifyInput = TmCardModifyInput()
    private var loaderDialog: LoaderDialog?=null
    private val tokomemberDashCreateViewModel: TokomemberDashCreateViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberDashCreateViewModel::class.java)
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
            throw RuntimeException(context.toString() )
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_create_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        tokomemberDashCreateViewModel.getCardInfo(arguments?.getInt("cardID")?:0)
        renderHeader()
        tipTokomember.setOnClickListener {
            Toast.makeText(context, "Click tips", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().build().inject(this)
    }

    private fun observeViewModel() {
        tokomemberDashCreateViewModel.tokomemberCardResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    mCardBgTemplateList.addAll(it.data.cardTemplateImageList as ArrayList<CardTemplateImageListItem>)
                    renderCardUi(it.data)
                }
                is Fail -> {
                    handleErrorUiOnErrorData()
                }
            }
        })

        tokomemberDashCreateViewModel.tokomemberCardBgResultLiveData.observe(
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

        tokomemberDashCreateViewModel.tokomemberCardColorResultLiveData.observe(
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

        tokomemberDashCreateViewModel.tokomemberCardModifyLiveData.observe(viewLifecycleOwner,{
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

    }

    private fun handleErrorUiOnUpdate(){
        val bundle = Bundle()
        val tmIntroBottomsheetModel = TmIntroBottomsheetModel(
            "Ada gangguan di rumah Toped",
            "Tunggu sebentar, biar Toped bereskan. Coba lagi atau kembali nanti.",
            "https://images.tokopedia.net/img/android/res/singleDpi/quest_widget_nonlogin_banner.png",
            "Coba Lagi"
        )
        bundle.putString(TokomemberBottomsheet.ARG_BOTTOMSHEET, Gson().toJson(tmIntroBottomsheetModel))
        TokomemberBottomsheet().setUpBottomSheetListener(this)
        TokomemberBottomsheet.show(bundle, childFragmentManager)
    }

    private fun openLoadingDialog(){

        loaderDialog = context?.let { LoaderDialog(it) }
        loaderDialog?.loaderText?.apply {
            setType(Typography.DISPLAY_2)
        }
        loaderDialog?.setLoadingText(Html.fromHtml(LOADING_TEXT))
        retryCount +=1
        loaderDialog?.show()
    }

    private fun closeLoadingDialog(){
        loaderDialog?.dialog?.dismiss()
    }

    private fun openProgramCreationPage() {
        val bundle = Bundle()
        bundle.putInt(BUNDLE_PROGRAM_TYPE, 0)
        bundle.putInt(BUNDLE_SHOP_ID, shopID)
        bundle.putParcelable(BUNDLE_CARD_DATA , tokomemberShopCardModel)
//        (activity as TokomemberDashCreateActivity).addFragment(
//            TokomemberProgramFragment.newInstance(
//                bundle
//            ), TAG_CARD_CREATE
//        )
        tmOpenFragmentCallback.openFragment(ProgramScreenType.PROGRAM, bundle)
    }

    private fun renderCardUi(data: CardDataTemplate) {
        shopID = data.card?.shopID?:0
        renderCardCarousel(data)
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
        btnContinueCard?.setOnClickListener {
            tokomemberDashCreateViewModel.modifyShopCard(mTmCardModifyInput)
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
            title = "Daftar TokoMember"
            subtitle = "Langkah 1 dari 4 "
            isShowBackButton = true
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
            tokomemberShopCardModel = TokomemberShopCardModel(
                shopName = data.card?.name ?: "",
                numberOfLevel = data.card?.numberOfLevel ?: 0,
                backgroundColor = data.cardTemplate?.backgroundColor ?: "",
                backgroundImgUrl = data.cardTemplate?.backgroundImgUrl ?: "",
                shopType = 0
            )
            shopViewPremium?.apply {
                setShopCardData(tokomemberShopCardModel)
            }
            shopViewVip?.apply {
                setShopCardData(tokomemberShopCardModel)
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

                tokomemberShopCardModel = TokomemberShopCardModel(
                    shopName = tokomemberShopCardModel.shopName,
                    numberOfLevel = tokomemberShopCardModel.numberOfLevel,
                    backgroundColor = tokomemberShopCardModel.backgroundColor,
                    backgroundImgUrl = tokoCardItem.imageUrl ?: "",
                    shopType = 0
                )
                shopViewPremium?.setShopCardData(tokomemberShopCardModel)
                shopViewVip?.setShopCardData(tokomemberShopCardModel)
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
        tokomemberDashCreateViewModel.tokomemberCardModifyLiveData.removeObservers(this)
    }

    override fun onButtonClick() {
        tokomemberDashCreateViewModel.modifyShopCard(mTmCardModifyInput)
    }

    companion object {
        const val PROGRAM_TYPE = "PROGRAM_TYPE"
        const val SHOP_ID = "SHOP_ID"
        const val TAG_CARD_CREATE = "CARD_CREATE"
        fun newInstance(bundle: Bundle): TokomemberCreateCardFragment {
            return TokomemberCreateCardFragment().apply {
                arguments = bundle
            }
        }
    }
}