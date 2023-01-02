package com.tokopedia.mvc.presentation.intro.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.tokopedia.utils.lifecycle.autoClearedNullable

class MvcIntroFragment : BaseDaggerFragment(), VoucherIntroViewMoreCustomView.ViewMoreListener {

    private var binding by autoClearedNullable<SmvcFragmentIntroBinding>()
    private var mvcAdapter: MvcIntroAdapter? = null
    private var contentList: List<Visitable<*>> = emptyList()
    private var mvcLayoutManager: LinearLayoutManager? = null

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

        mvcLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView?.layoutManager = mvcLayoutManager

        mvcAdapter?.clearAllElements()
        mvcAdapter?.addElement(contentList)

        binding?.recyclerView?.apply {
            adapter = mvcAdapter
        }
    }

    private fun HeaderUnify.setupHeader() {
        title = context.getString(R.string.smvc_intro_voucher_toolbar_text)
        setNavigationOnClickListener {
            activity?.finish()
        }
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
                VoucherIntroCarouselUiModel(
                    headerTitle = it.getString(R.string.smvc_intro_voucher_view_pager_header),
                    description = it.getString(
                        R.string.smvc_intro_voucher_view_pager_tab_description
                    ),
                    tabsList = listOf(
                        VoucherIntroCarouselUiModel.VoucherIntroTabsData(
                            it.getString(R.string.smvc_intro_voucher_view_pager_tab_1_title),
                            listOf()
                        ),
                        VoucherIntroCarouselUiModel.VoucherIntroTabsData(
                            it.getString(R.string.smvc_intro_voucher_view_pager_tab_2_title),
                            listOf()
                        ),
                        VoucherIntroCarouselUiModel.VoucherIntroTabsData(
                            it.getString(R.string.smvc_intro_voucher_view_pager_tab_3_title),
                            listOf()
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
       // scrollListener?.enableRecyclerViewScroll(binding?.recyclerView)
    }
}
