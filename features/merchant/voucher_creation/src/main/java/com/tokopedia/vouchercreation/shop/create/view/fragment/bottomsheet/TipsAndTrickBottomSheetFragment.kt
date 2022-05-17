package com.tokopedia.vouchercreation.shop.create.view.fragment.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.databinding.MvcVoucherBottomSheetViewBinding
import com.tokopedia.vouchercreation.shop.create.data.source.TipsAndTrickStaticDataSource
import com.tokopedia.vouchercreation.shop.create.view.adapter.vouchertarget.VoucherTipsAdapter
import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherCreationStep
import javax.inject.Inject

class TipsAndTrickBottomSheetFragment : BottomSheetUnify() {

    companion object {

        const val TAG = "TipsAndTrickBottomSheet"

        fun createInstance() : TipsAndTrickBottomSheetFragment = TipsAndTrickBottomSheetFragment()
    }

    private var binding by autoClearedNullable<MvcVoucherBottomSheetViewBinding>()

    @Inject
    lateinit var userSession: UserSessionInterface

    private val voucherTipsAdapter by lazy {
        context?.run {
            VoucherTipsAdapter(
                    TipsAndTrickStaticDataSource.getTipsAndTrickUiModelList(),
                    ::onChevronAltered,
                    ::onItemClicked)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isAdded) {
            initView()
        }
    }

    private fun initBottomSheet() {
        binding = MvcVoucherBottomSheetViewBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        setTitle(getString(R.string.mvc_create_tips_bottomsheet_title).toBlankOrString())
    }

    private fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    private fun initView() {
        view?.setupBottomSheetChildNoMargin()
        binding?.voucherTipsRecyclerView?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = voucherTipsAdapter
        }
    }

    private fun View.setupBottomSheetChildNoMargin() {
        val initialPaddingTop = paddingTop
        val initialPaddingBottom = paddingBottom
        val initialPaddingLeft = paddingLeft
        val initialPaddingRight = paddingRight
        setPadding(0,initialPaddingTop,0,initialPaddingBottom)
        bottomSheetHeader.setPadding(initialPaddingLeft, 0, initialPaddingRight, 0)
    }


    private fun onChevronAltered(position: Int) {
        voucherTipsAdapter?.itemList?.get(position)?.run {
            isOpen = !isOpen
        }
        voucherTipsAdapter?.notifyItemChanged(position)
    }

    private fun onItemClicked(@StringRes titleRes: Int) {
        VoucherCreationTracking.sendCreateVoucherClickTracking(
                step = VoucherCreationStep.TARGET,
                action =
                        when(titleRes) {
                            R.string.mvc_tips_title_cashback_benefit -> VoucherCreationAnalyticConstant.EventAction.Click.TIPS_TRICK_VOUCHER_TYPE
                            R.string.mvc_create_tips_title_voucher_name -> VoucherCreationAnalyticConstant.EventAction.Click.TIPS_TRICK_VOUCHER_NAME
                            R.string.mvc_create_tips_title_cashback_type -> VoucherCreationAnalyticConstant.EventAction.Click.TIPS_TRICK_CASHBACK_TYPE
                            R.string.mvc_create_tips_title_voucher_max_estimation -> VoucherCreationAnalyticConstant.EventAction.Click.TIPS_TRICK_SPENDING_ESTIMATION
                            else -> ""
                        },
                userId = userSession.userId
        )
    }

}