package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.tokomember_common_widget.TokomemberShopView
import com.tokopedia.tokomember_common_widget.model.TokomemberShopCardModel
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.Card
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.CardTemplate
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.IntoolsShop
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCardModifyInput
import com.tokopedia.tokomember_seller_dashboard.model.CardDataTemplate
import com.tokopedia.tokomember_seller_dashboard.model.CardTemplateImageListItem
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_TYPE
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashCreateActivity
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
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashCreateViewModel
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.tm_dash_create_card.*
import javax.inject.Inject

class TokomemberCreateCardFragment : BaseDaggerFragment(), TokomemberCardColorAdapterListener,
    TokomemberCardBgAdapterListener {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private var isColorPalleteClicked = false
    private var mCardBgTemplateList = arrayListOf<CardTemplateImageListItem>()
    var shopViewPremium: TokomemberShopView? = null
    var shopViewVip: TokomemberShopView? = null
    private var shopID = 0
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
                    }
                }
            })

        tokomemberDashCreateViewModel.tokomemberCardModifyLiveData.observe(viewLifecycleOwner,{
            when(it) {
                is Success -> {
                     openProgramCreationPage()
                }
                is Fail -> {
                    view?.let { v -> Toaster.build(v,it.throwable.localizedMessage?:"",Toaster.LENGTH_LONG , Toaster.TYPE_ERROR).show()
                    }
                }
            }
        })
    }

    private fun openProgramCreationPage() {
        val bundle = Bundle()
        bundle.putInt(BUNDLE_PROGRAM_TYPE, 0)
        bundle.putInt(BUNDLE_SHOP_ID, shopID)
        (activity as TokomemberDashCreateActivity).addFragment(
            TokomemberProgramFragment.newInstance(
                bundle
            ), TAG_PROGRAM_CREATE
        )
    }

    private fun renderCardUi(data: CardDataTemplate) {
        shopID = data.card?.shopID?:0
        renderCardCarousel(data)
        btnContinueCard?.setOnClickListener {
            tokomemberDashCreateViewModel.modifyShopCard(
                TmCardModifyInput(
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
            )
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
            shopViewPremium?.apply {
                setShopCardData(
                    TokomemberShopCardModel(
                        shopName = data.card?.name ?: "",
                        numberOfLevel = data.card?.numberOfLevel ?: 0,
                        backgroundColor = data.cardTemplate?.backgroundColor ?: "",
                        backgroundImgUrl = data.cardTemplate?.backgroundImgUrl ?: "",
                        shopType = 0
                    )
                )
            }

            shopViewVip?.apply {
                setShopCardData(
                    TokomemberShopCardModel(
                        shopName = data.card?.name ?: "",
                        numberOfLevel = data.card?.numberOfLevel ?: 0,
                        backgroundColor = data.cardTemplate?.backgroundColor ?: "",
                        backgroundImgUrl = data.cardTemplate?.backgroundImgUrl ?: "",
                        shopType = 1
                    )
                )
            }

            carouselCard.apply {
                indicatorPosition = CarouselUnify.INDICATOR_BC
                slideToShow = 1f
                slideToScroll = 1
                freeMode = false
                centerMode = true
                autoplay = false
                addItem(shopViewPremium!!)
                addItem(shopViewVip!!)
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
                shopViewPremium?.setShopCardData(
                    TokomemberShopCardModel(
                        shopName = "kk",
                        backgroundImgUrl = tokoCardItem.imageUrl ?: "",
                        shopType = 0
                    )
                )
                shopViewVip?.setShopCardData(
                    TokomemberShopCardModel(
                        shopName = "kk",
                        backgroundImgUrl = tokoCardItem.imageUrl ?: "",
                        shopType = 1
                    )
                )
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

    companion object {
        const val PROGRAM_TYPE = "PROGRAM_TYPE"
        const val SHOP_ID = "SHOP_ID"
        const val TAG_PROGRAM_CREATE = "Program_Create"
        fun newInstance(bundle: Bundle): TokomemberCreateCardFragment {
            return TokomemberCreateCardFragment().apply {
                arguments = bundle
            }
        }
    }
}