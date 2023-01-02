package com.tokopedia.mvc.presentation.intro.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcIntroFragmentBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.presentation.intro.adapter.MvcIntroAdapter
import com.tokopedia.mvc.presentation.intro.uimodel.ChoiceOfVoucherUiModel
import com.tokopedia.mvc.presentation.intro.uimodel.IntroVoucherUiModel
import com.tokopedia.mvc.presentation.intro.uimodel.VoucherIntroCarouselUiModel
import com.tokopedia.mvc.presentation.intro.uimodel.VoucherIntroTypeData
import com.tokopedia.mvc.presentation.intro.uimodel.VoucherTypeUiModel
import com.tokopedia.utils.lifecycle.autoClearedNullable

class MvcIntroFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<SmvcIntroFragmentBinding>()
    private var adapter: MvcIntroAdapter? = null
    private var contentList: List<Visitable<*>> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcIntroFragmentBinding.inflate(LayoutInflater.from(context))
        initInjector()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.recyclerView?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        this.contentList = getContentList()
        adapter = MvcIntroAdapter()

        adapter?.clearAllElements()
        adapter?.addElement(contentList)

        binding?.recyclerView?.adapter = adapter
    }

    private fun getContentList(): List<Visitable<*>> {
        return context?.resources?.let {
            listOf(
                IntroVoucherUiModel(
                    it.getString(R.string.smvc_intro_make_coupons_faster),
                    it.getString(R.string.smvc_intro_coupon_subtitle),
                    list = listOf(
                        VoucherIntroTypeData(
                            it.getString(R.string.smvc_intro_coupon_card_1_title),
                            it.getString(R.string.smvc_intro_coupon_card_1_subtitle),
                            ""
                        ),
                        VoucherIntroTypeData(
                            it.getString(R.string.smvc_intro_coupon_card_2_title),
                            it.getString(R.string.smvc_intro_coupon_card_2_subtitle),
                            ""
                        ),
                        VoucherIntroTypeData(
                            it.getString(R.string.smvc_intro_coupon_card_3_title),
                            it.getString(R.string.smvc_intro_coupon_card_3_subtitle),
                            ""
                        )

                    )
                ),
                VoucherTypeUiModel(
                    title = it.getString(R.string.smvc_intro_coupon_type_card_title),
                    list = listOf(
                        VoucherIntroTypeData(
                            it.getString(R.string.smvc_intro_coupon_type_card_1_title),
                            it.getString(R.string.smvc_intro_coupon_type_card_1_subtitle),
                        ),
                        VoucherIntroTypeData(
                            it.getString(R.string.smvc_intro_coupon_type_card_2_title),
                            it.getString(R.string.smvc_intro_coupon_type_card_2_subtitle),
                        )
                    )
                ),
                VoucherIntroCarouselUiModel(
                    headerTitle = it.getString(R.string.smvc_intro_coupon_view_pager_header),
                    description = it.getString(R.string.smvc_intro_coupon_view_pager_tab_description),
                    tabsList = listOf(
                        VoucherIntroCarouselUiModel.VoucherIntroTabsData(
                            it.getString(R.string.smvc_intro_coupon_view_pager_tab_1_title),
                            listOf()
                        ),
                        VoucherIntroCarouselUiModel.VoucherIntroTabsData(
                            it.getString(R.string.smvc_intro_coupon_view_pager_tab_2_title),
                            listOf()
                        ),
                        VoucherIntroCarouselUiModel.VoucherIntroTabsData(
                            it.getString(R.string.smvc_intro_coupon_view_pager_tab_3_title),
                            listOf()
                        ),
                    )
                ),
                ChoiceOfVoucherUiModel(
                    it.getString(R.string.smvc_intro_coupon_type_choice_of_target),
                    list = listOf(
                        VoucherIntroTypeData(
                            it.getString(R.string.smvc_intro_coupon_type_choice_of_target_card_1_title),
                            it.getString(R.string.smvc_intro_coupon_type_choice_of_target_card_1_subtitle),
                            ""
                        ),
                        VoucherIntroTypeData(
                            it.getString(R.string.smvc_intro_coupon_type_choice_of_target_card_2_title),
                            it.getString(R.string.smvc_intro_coupon_type_choice_of_target_card_2_subtitle),
                            ""
                        ),
                    )
                ),

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
}
