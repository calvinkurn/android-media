package com.tokopedia.mvc.presentation.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.mvc.R
import androidx.lifecycle.observe
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.databinding.SmvcFragmentVoucherDetailBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailHeaderSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailProductSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailVoucherInfoSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailVoucherSettingSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailVoucherTypeSectionBinding
import com.tokopedia.mvc.domain.entity.VoucherDetailData
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.Date
import javax.inject.Inject

class VoucherDetailFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance(couponId: Long): VoucherDetailFragment {
            return VoucherDetailFragment().apply {
                arguments = Bundle().apply {

                }
            }
        }
    }

    //binding
    private var binding by autoClearedNullable<SmvcFragmentVoucherDetailBinding>()
    private var headerBinding by autoClearedNullable<SmvcVoucherDetailHeaderSectionBinding>()
    private var voucherTypeBinding by autoClearedNullable<SmvcVoucherDetailVoucherTypeSectionBinding>()
    private var voucherInfoBinding by autoClearedNullable<SmvcVoucherDetailVoucherInfoSectionBinding>()
    private var voucherSettingBinding by autoClearedNullable<SmvcVoucherDetailVoucherSettingSectionBinding>()
    private var voucherProductBinding by autoClearedNullable<SmvcVoucherDetailProductSectionBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(VoucherDetailViewModel::class.java) }

    override fun getScreenName(): String =
        VoucherDetailFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcFragmentVoucherDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        getVoucherDetailData(14715615)
    }

    private fun observeData() {
        viewModel.voucherDetail.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    setupView(result.data)
                }
                is Fail -> {

                }
            }
        }
    }

    private fun setupView(data: VoucherDetailData) {
        binding?.run {
            layoutHeader.inflate()
            layoutVoucherType.inflate()
            layoutVoucherInfo.inflate()
            layoutVoucherSetting.inflate()
            layoutProductList.inflate()
        }
        setupHeaderSection(data)
    }

    private fun setupHeaderSection(data: VoucherDetailData) {
        binding?.run {
            header.headerTitle = data.voucherName
        }
        headerBinding?.run {
            setupVoucherStatus(data)
            imgVoucher.setImageUrl(data.voucherImage)
            when (data.isVps) {
                0 -> {
                    labelVoucherSource.gone()
                    tpgVpsPackage.gone()
                }
                else -> {
                    labelVoucherSource.apply {
                        visible()
                        text = "Paket Promosi"
                    }
                    tpgVpsPackage.apply {
                        visible()
                        text = data.packageName
                    }
                }
            }
            val availableQuota = data.apply { voucherQuota - remainingQuota }
            tpgUsedVoucherQuota.text = availableQuota.toString()
            tpgAvailableVoucherQuota.text = data.remainingQuota.toString()
        }
    }

    private fun setupVoucherStatus(data: VoucherDetailData) {
        headerBinding?.run {
            tpgVoucherStatus.apply {
                when (data.voucherStatus) {
                    1 -> {
                        text = "Mendatang"
                        setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                    }
                    2 -> {
                        text = "Berlangsung"
                        setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Green_G500)
                    }
                    3 -> {
                        text = "Berakhir"
                        setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                    }
                    4 -> {
                        text = "Dihentikan"
                        setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Red_R500)
                    }
                }
            }
        }
    }

    private fun setupVoucherAction(data: VoucherDetailData) {
        headerBinding?.run {
            when (data.voucherStatus) {
                1 -> {
                    btnUbahKupon.visible()
                    timer.invisible()
                    tpgPeriodStop.invisible()
                }
                2 -> {
                    btnUbahKupon.invisible()
                    timer.visible()
                    tpgPeriodStop.invisible()
                    startTimer(Date(data.voucherFinishTime))
                }
                3 -> {
                    btnUbahKupon.invisible()
                    timer.invisible()
                    tpgPeriodStop.invisible()
                }
                else -> {
                    val stoppedDate = Date(data.updateTime)
                    btnUbahKupon.invisible()
                    timer.invisible()
                    tpgPeriodStop.apply {
                        visible()
                        text = getString(
                            R.string.smvc_placeholder_stopped_date,
                            stoppedDate.formatTo(DateConstant.DATE_YEAR_PRECISION)
                        )
                    }
                }
            }
        }
    }

    private fun startTimer(endDate: Date) {
        headerBinding?.run {
            timer.timerFormat = TimerUnifySingle.FORMAT_AUTO
            timer.timerVariant = TimerUnifySingle.VARIANT_GENERAL
            timer.targetDate = endDate.toCalendar()
        }
    }

    private fun getVoucherDetailData(voucherId: Long) {
        viewModel.getVoucherDetail(voucherId)
    }
}
