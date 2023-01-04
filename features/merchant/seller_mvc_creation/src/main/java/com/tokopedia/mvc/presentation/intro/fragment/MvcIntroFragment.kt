package com.tokopedia.mvc.presentation.intro.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
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
import com.tokopedia.mvc.presentation.intro.util.FIRST_INDEX
import com.tokopedia.mvc.presentation.intro.util.MvcIntroRecyclerViewScrollListener
import com.tokopedia.utils.lifecycle.autoClearedNullable

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
                IntroVoucherUiModel(
                    it.getString(R.string.smvc_intro_make_coupons_faster),
                    it.getString(R.string.smvc_intro_coupon_subtitle),
                    list = listOf(
                        VoucherIntroTypeData(
                            it.getString(R.string.smvc_intro_voucher_card_1_title),
                            it.getString(R.string.smvc_intro_voucher_card_1_subtitle),
                            it.getString(R.string.smvc_intro_voucher_icon_fleksibel_buat_kupon)
                        ),
                        VoucherIntroTypeData(
                            it.getString(R.string.smvc_intro_voucher_card_2_title),
                            it.getString(R.string.smvc_intro_voucher_card_2_subtitle),
                            it.getString(R.string.smvc_intro_voucher_icon_beragam_pilihan_promo)
                        ),
                        VoucherIntroTypeData(
                            it.getString(R.string.smvc_intro_voucher_card_3_title),
                            it.getString(R.string.smvc_intro_voucher_card_3_subtitle),
                            it.getString(R.string.smvc_intro_voucher_icon_bebas_tentukan_target)
                        )

                    )
                ),
                VoucherTypeUiModel(
                    title = it.getString(R.string.smvc_intro_voucher_type_card_title),
                    list = listOf(
                        VoucherIntroTypeData(
                            it.getString(R.string.smvc_intro_voucher_type_card_1_title),
                            it.getString(R.string.smvc_intro_voucher_type_card_1_subtitle),
                            it.getString(R.string.smvc_intro_voucher_icon_kupon_toko)
                        ),
                        VoucherIntroTypeData(
                            it.getString(R.string.smvc_intro_voucher_type_card_2_title),
                            it.getString(R.string.smvc_intro_voucher_type_card_2_subtitle),
                            it.getString(R.string.smvc_intro_voucher_icon_kupon_produk)
                        )
                    )
                ),
                // TODO FIX THE URLS
                VoucherIntroCarouselUiModel(
                    headerTitle = it.getString(R.string.smvc_intro_voucher_view_pager_header),
                    description = it.getString(
                        R.string.smvc_intro_voucher_view_pager_tab_description
                    ),
                    tabsList = listOf(
                        VoucherIntroCarouselUiModel.VoucherIntroTabsData(
                            it.getString(R.string.smvc_intro_voucher_view_pager_tab_1_title),
                            listOf(
                                it.getString(R.string.smvc_intro_voucher_carousel_cashback_1),
                                it.getString(R.string.smvc_intro_voucher_carousel_cashback_1)
                                )
                        ),
                        VoucherIntroCarouselUiModel.VoucherIntroTabsData(
                            it.getString(R.string.smvc_intro_voucher_view_pager_tab_2_title),
                            listOf(
                                it.getString(R.string.smvc_intro_voucher_carousel_gratis_ongkir_1),
                                it.getString(R.string.smvc_intro_voucher_carousel_gratis_ongkir_2)
                            )
                        ),
                        VoucherIntroCarouselUiModel.VoucherIntroTabsData(
                            it.getString(R.string.smvc_intro_voucher_view_pager_tab_3_title),
                            listOf(
                                it.getString(R.string.smvc_intro_voucher_carousel_diskon_1),
                                it.getString(R.string.smvc_intro_voucher_carousel_diskon_2)
                            )
                        )
                    )
                ),
                ChoiceOfVoucherUiModel(
                    it.getString(R.string.smvc_intro_voucher_type_choice_of_target),
                    list = listOf(
                        VoucherIntroTypeData(
                            it.getString(
                                R.string.smvc_intro_voucher_type_choice_of_target_card_1_title
                            ),
                            it.getString(
                                R.string.smvc_intro_voucher_type_choice_of_target_card_1_subtitle
                            ),
                            it.getString(R.string.smvc_intro_voucher_icon_semua_pembeli)
                        ),
                        VoucherIntroTypeData(
                            it.getString(
                                R.string.smvc_intro_voucher_type_choice_of_target_card_2_title
                            ),
                            it.getString(
                                R.string.smvc_intro_voucher_type_choice_of_target_card_2_subtitle
                            ),
                            it.getString(R.string.smvc_intro_voucher_icon_follower_baru)
                        )
                    )
                )

            )
        } ?: emptyList()
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

    override fun enableRVScroll() {
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
}
