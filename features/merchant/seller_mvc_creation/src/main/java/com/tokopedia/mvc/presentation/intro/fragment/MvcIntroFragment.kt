package com.tokopedia.mvc.presentation.intro.fragment

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcFragmentIntroBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.presentation.intro.adapter.MvcIntroAdapter
import com.tokopedia.mvc.presentation.intro.customviews.VoucherIntroViewMoreCustomView
import com.tokopedia.mvc.presentation.intro.uimodel.ChoiceOfVoucherUiModel
import com.tokopedia.mvc.presentation.intro.uimodel.IntroVoucherUiModel
import com.tokopedia.mvc.presentation.intro.uimodel.VoucherIntroCarouselUiModel
import com.tokopedia.mvc.presentation.intro.uimodel.VoucherIntroTypeData
import com.tokopedia.mvc.presentation.intro.uimodel.VoucherTypeUiModel
import com.tokopedia.mvc.presentation.intro.util.MvcIntroPageTracker
import com.tokopedia.mvc.presentation.intro.util.MvcIntroRecyclerViewScrollListener
import com.tokopedia.mvc.util.constant.FIRST_INDEX
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class MvcIntroFragment :
    BaseDaggerFragment(),
    VoucherIntroViewMoreCustomView.ViewMoreListener {

    private var binding by autoClearedNullable<SmvcFragmentIntroBinding>(onClear = {
        scrollerListener?.let { listener -> it.recyclerView.removeOnScrollListener(listener) }
    })

    private var mvcAdapter: MvcIntroAdapter? = null
    private var contentList: List<Visitable<*>> = emptyList()
    private var mvcLayoutManager: LinearLayoutManager? = null
    private var scrollerListener: MvcIntroRecyclerViewScrollListener? = null

    @Inject
    lateinit var mvcIntroPageTracker: MvcIntroPageTracker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcFragmentIntroBinding.inflate(LayoutInflater.from(context))
        initInjector()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            header.setupHeader()
            btnViewMore.setUpListener(this@MvcIntroFragment)
        }
        this.contentList = getContentList()
        mvcAdapter = MvcIntroAdapter()

        mvcLayoutManager = object : LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        ) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding?.recyclerView?.layoutManager = mvcLayoutManager
        mvcAdapter?.clearAllElements()
        mvcAdapter?.addElement(contentList)

        binding?.recyclerView?.apply {
            adapter = mvcAdapter
            initRvScroller(this)
        }
        binding?.btnCreateVoucher?.setOnClickListener {
            mvcIntroPageTracker.sendMvcIntroPageCreateVoucherEvent()
            RouteManager.route(context, CREATE_VOUCHER_SHOP_APPLINK)
        }
    }

    private fun initRvScroller(recyclerView: RecyclerView) {
        scrollerListener = object : MvcIntroRecyclerViewScrollListener(mvcLayoutManager) {
            override fun changeBackground(position: Int) {
                changeBackgroundWithPosition(position)
            }
        }.also {
            recyclerView.addOnScrollListener(it)
        }
    }

    private fun changeBackgroundWithPosition(position: Int) {
        if (position == RecyclerView.NO_POSITION) return
        context?.resources?.let {
            if (position == 0) {
                setGreenBackgroundForToolbar()
            } else {
                setWhiteBackgroundForToolbar()
            }
        }
    }

    private fun HeaderUnify.setupHeader() {
        title = context.getString(R.string.smvc_intro_voucher_toolbar_text)
        setNavigationOnClickListener {
            activity?.finish()
        }
        setGreenBackgroundForToolbar()
    }

    private fun getContentList(): List<Visitable<*>> {
        return context?.resources?.let {
            listOf(
                it.getIntroVoucher(),
                it.getIntroTypeData(),
                it.getIntroCarouselData(),
                it.getChoiceOfVoucherData()
            )
        } ?: emptyList()
    }

    private fun Resources.getIntroVoucher(): IntroVoucherUiModel {
        return IntroVoucherUiModel(
            getString(R.string.smvc_intro_make_coupons_faster),
            getString(R.string.smvc_intro_coupon_subtitle),
            list = listOf(
                VoucherIntroTypeData(
                    getString(R.string.smvc_intro_voucher_card_1_title),
                    getString(R.string.smvc_intro_voucher_card_1_subtitle),
                    getString(R.string.smvc_intro_voucher_icon_fleksibel_buat_kupon)
                ),
                VoucherIntroTypeData(
                    getString(R.string.smvc_intro_voucher_card_2_title),
                    getString(R.string.smvc_intro_voucher_card_2_subtitle),
                    getString(R.string.smvc_intro_voucher_icon_beragam_pilihan_promo)
                ),
                VoucherIntroTypeData(
                    getString(R.string.smvc_intro_voucher_card_3_title),
                    getString(R.string.smvc_intro_voucher_card_3_subtitle),
                    getString(R.string.smvc_intro_voucher_icon_bebas_tentukan_target)
                )
            )
        )
    }

    private fun Resources.getIntroTypeData(): VoucherTypeUiModel {
        return VoucherTypeUiModel(
            title = getString(R.string.smvc_intro_voucher_type_card_title),
            list = listOf(
                VoucherIntroTypeData(
                    getString(R.string.smvc_intro_voucher_type_card_1_title),
                    getString(R.string.smvc_intro_voucher_type_card_1_subtitle),
                    getString(R.string.smvc_intro_voucher_icon_kupon_toko)
                ),
                VoucherIntroTypeData(
                    getString(R.string.smvc_intro_voucher_type_card_2_title),
                    getString(R.string.smvc_intro_voucher_type_card_2_subtitle),
                    getString(R.string.smvc_intro_voucher_icon_kupon_produk)
                )
            )
        )
    }

    private fun Resources.getIntroCarouselData(): VoucherIntroCarouselUiModel {
        return VoucherIntroCarouselUiModel(
            headerTitle = getString(R.string.smvc_intro_voucher_view_pager_header),
            tabsList = listOf(
                VoucherIntroCarouselUiModel.VoucherIntroTabsData(
                    getString(R.string.smvc_intro_voucher_view_pager_tab_1_title),
                    getString(R.string.smvc_intro_voucher_view_pager_tab_1_description),
                    listOf(
                        getString(R.string.smvc_intro_voucher_carousel_cashback_1),
                        getString(R.string.smvc_intro_voucher_carousel_cashback_2)
                    )
                ),
                VoucherIntroCarouselUiModel.VoucherIntroTabsData(
                    getString(R.string.smvc_intro_voucher_view_pager_tab_2_title),
                    getString(R.string.smvc_intro_voucher_view_pager_tab_2_description),
                    listOf(
                        getString(R.string.smvc_intro_voucher_carousel_gratis_ongkir_1),
                        getString(R.string.smvc_intro_voucher_carousel_gratis_ongkir_2)
                    )
                ),
                VoucherIntroCarouselUiModel.VoucherIntroTabsData(
                    getString(R.string.smvc_intro_voucher_view_pager_tab_3_title),
                    getString(R.string.smvc_intro_voucher_view_pager_tab_3_description),
                    listOf(
                        getString(R.string.smvc_intro_voucher_carousel_diskon_1),
                        getString(R.string.smvc_intro_voucher_carousel_diskon_2)
                    )
                )
            )
        )
    }

    private fun Resources.getChoiceOfVoucherData(): ChoiceOfVoucherUiModel {
        return ChoiceOfVoucherUiModel(
            getString(R.string.smvc_intro_voucher_type_choice_of_target),
            list = listOf(
                VoucherIntroTypeData(
                    getString(
                        R.string.smvc_intro_voucher_type_choice_of_target_card_1_title
                    ),
                    getString(
                        R.string.smvc_intro_voucher_type_choice_of_target_card_1_subtitle
                    ),
                    getString(R.string.smvc_intro_voucher_icon_semua_pembeli)
                ),
                VoucherIntroTypeData(
                    getString(
                        R.string.smvc_intro_voucher_type_choice_of_target_card_2_title
                    ),
                    getString(
                        R.string.smvc_intro_voucher_type_choice_of_target_card_2_subtitle
                    ),
                    getString(R.string.smvc_intro_voucher_icon_follower_baru)
                )
            )
        )
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent(
                (activity?.applicationContext as? BaseMainApplication)?.baseAppComponent
            )
            .build()
            .inject(this)
    }

    // When user clicks on the Arrow Button
    override fun enableRVScroll() {
        mvcIntroPageTracker.sendMvcIntroPageArrowButton()
        mvcLayoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
            override fun canScrollVertically(): Boolean {
                return true
            }
        }

        val recyclerView = binding?.recyclerView ?: return
        scrollerListener?.let { recyclerView.removeOnScrollListener(it) }
        scrollerListener = object : MvcIntroRecyclerViewScrollListener(mvcLayoutManager) {
            override fun changeBackground(position: Int) {
                changeBackgroundWithPosition(position)
            }
        }.also {
            recyclerView.addOnScrollListener(it)
        }

        binding?.recyclerView?.apply {
            layoutManager = mvcLayoutManager
            layoutManager?.scrollToPosition(FIRST_INDEX)
        }

        setWhiteBackgroundForToolbar()
    }

    private fun setWhiteBackgroundForToolbar() {
        context?.resources?.getColor(com.tokopedia.unifyprinciples.R.color.Unify_Header_Background)
            ?.let {
                binding?.header?.setBackgroundColor(it)
                activity?.window?.statusBarColor = it
            }
    }

    private fun setGreenBackgroundForToolbar() {
        context?.resources?.getColor(R.color.mvc_dms_toolbar_color)?.let {
            binding?.header?.setBackgroundColor(it)
            activity?.window?.statusBarColor = it
        }
    }

    companion object {
        private const val CREATE_VOUCHER_SHOP_APPLINK = "sellerapp://seller-mvc/create/shop"
    }
}
