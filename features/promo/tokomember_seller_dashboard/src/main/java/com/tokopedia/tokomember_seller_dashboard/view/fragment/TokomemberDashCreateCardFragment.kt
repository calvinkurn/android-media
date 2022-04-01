package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.tokomember_common_widget.TokomemberShopView
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.CardTemplateImageListItem
import com.tokopedia.tokomember_seller_dashboard.model.ColorTemplateListItem
import com.tokopedia.tokomember_seller_dashboard.model.MembershipGetCardForm
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberDashCardColorAdapter
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberDashCardColorBgAdapter
import com.tokopedia.tokomember_seller_dashboard.view.adapter.decoration.TokomemberDashColorItemDecoration
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashCreateCardViewModel
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.tm_dash_create_card.*
import javax.inject.Inject

class TokomemberDashCreateCardFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tokomemberDashCreateCardViewModel: TokomemberDashCreateCardViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberDashCreateCardViewModel::class.java)
    }
    private val tokomemberDashCardColorAdapter: TokomemberDashCardColorAdapter by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        TokomemberDashCardColorAdapter(arrayListOf())
    }

    private val tokomemberDashCardColorBgAdapter: TokomemberDashCardColorBgAdapter by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        TokomemberDashCardColorBgAdapter(arrayListOf())
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
        tokomemberDashCreateCardViewModel.getCardInfo(0)
        renderHeader()
        renderCardCarousel()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().build().inject(this)
    }

    private fun observeViewModel() {
        tokomemberDashCreateCardViewModel.tokomemberCardResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    renderUi(it.data.membershipGetCardForm)
                }
                is Fail -> {

                }
            }
        })
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

    private fun renderCardCarousel() {
        carouselCard.apply {
            indicatorPosition = CarouselUnify.INDICATOR_BC
            freeMode = false
            centerMode = false
            addView(TokomemberShopView(context))
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

    @SuppressLint("NotifyDataSetChanged")
    private fun renderUi(membershipGetCardForm: MembershipGetCardForm?) {
        val layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        rvColor.layoutManager = layoutManager
        rvColor.adapter = tokomemberDashCardColorAdapter
        if (rvColor.itemDecorationCount.isZero()){
            rvColor.addItemDecoration(TokomemberDashColorItemDecoration(dpToPx(12).toInt()))
        }
        tokomemberDashCardColorAdapter.cardColorList = membershipGetCardForm?.colorTemplateList as ArrayList<ColorTemplateListItem>
        tokomemberDashCardColorAdapter.notifyDataSetChanged()

        val layoutManagerBg = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        rvCardBg.layoutManager = layoutManagerBg
        rvCardBg.adapter = tokomemberDashCardColorBgAdapter
        if (rvCardBg.itemDecorationCount.isZero()){
            rvCardBg.addItemDecoration(TokomemberDashColorItemDecoration(dpToPx(12).toInt()))
        }
        tokomemberDashCardColorBgAdapter.cardColorList = membershipGetCardForm?.cardTemplateImageList as ArrayList<CardTemplateImageListItem>
        tokomemberDashCardColorBgAdapter.notifyDataSetChanged()
    }

    companion object {

        fun newInstance(): TokomemberDashCreateCardFragment {
            return TokomemberDashCreateCardFragment()
        }

    }
}