package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.domain.TM_PROGRAM_FORM
import com.tokopedia.tokomember_seller_dashboard.model.MembershipGetProgramForm
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_DETAIL
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.PROGRAM_DETAIL_ACTIVE
import com.tokopedia.tokomember_seller_dashboard.util.PROGRAM_DETAIL_WAITING
import com.tokopedia.tokomember_seller_dashboard.util.TM_DETAIL_BG
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmDashCreateViewModel
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import kotlinx.android.synthetic.main.tm_dash_program_detail_fragment.*
import javax.inject.Inject

class TokomemberDashProgramDetailFragment : BaseDaggerFragment() {

    private var shopId = 0
    private var programId = 0
    private var tmTracker : TmTracker? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tmDashCreateViewModel: TmDashCreateViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TmDashCreateViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt(BUNDLE_SHOP_ID, 0)?.let {
            shopId = it
        }
        arguments?.getInt(BUNDLE_PROGRAM_ID, 0)?.let {
            programId = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_program_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tmTracker = TmTracker()
        tmTracker?.viewProgramDetail(shopId.toString(), programId.toString())

        Glide.with(linear_top)
            .asDrawable()
            .load(TM_DETAIL_BG)
            .into(object : CustomTarget<Drawable>(){
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    linear_top.background = resource
                }
                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
        observeViewModel()
        tmDashCreateViewModel.getProgramInfo(programId, shopId, ACTION_DETAIL, TM_PROGRAM_FORM)
        setHeader()
    }

    private fun observeViewModel() {
        tmDashCreateViewModel.tmProgramResultLiveData.observe(viewLifecycleOwner,{
            when(it.status){
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    setMainUi(it.data?.membershipGetProgramForm)
                }
                TokoLiveDataResult.STATUS.ERROR -> {

                }
            }
        })
    }
    private fun setMainUi(membershipGetProgramForm: MembershipGetProgramForm?) {
        val startDateString = membershipGetProgramForm?.programForm?.timeWindow?.startTime?.let {
            TmDateUtil.setDatePreview(
                it
            )
        }

        val endDateString = membershipGetProgramForm?.programForm?.timeWindow?.endTime?.let {
            TmDateUtil.setDatePreview(
                it
            )
        }

        val startTimeString = membershipGetProgramForm?.programForm?.timeWindow?.startTime?.let {
            TmDateUtil.setTime(
                it
            )
        }

        val endTimeString = membershipGetProgramForm?.programForm?.timeWindow?.endTime?.let {
            TmDateUtil.setTime(
                it
            )
        }
        val finalString = "$startDateString, $startTimeString - $endDateString, $endTimeString"
        tvProgramPeriod.text = getDateTimeSpannable(finalString,startDateString?.length ?: 0,endDateString?.length ?: 0)

        tvMemberCount.text = membershipGetProgramForm?.programForm?.analytics?.totalNewMember
        tvProgramStatus.visibility = View.VISIBLE
        tvProgramStatus.text = membershipGetProgramForm?.programForm?.statusStr
        when(membershipGetProgramForm?.programForm?.statusStr){
            PROGRAM_DETAIL_ACTIVE ->{
                childFragmentManager.beginTransaction().add(R.id.frameCouponList, TokomemberDashCouponFragment.newInstance(arguments, true), tag).addToBackStack(tag).commit()
                tvProgramStatus.backgroundTintList = context?.let {
                    ContextCompat.getColor(
                        it, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                }?.let { ColorStateList.valueOf(it) }
            }
            PROGRAM_DETAIL_WAITING -> {
                childFragmentManager.beginTransaction().add(R.id.frameCouponList, TokomemberDashCouponFragment.newInstance(arguments, false), tag).addToBackStack(tag).commit()
                tvProgramStatus.backgroundTintList = context?.let {
                    ContextCompat.getColor(
                        it, com.tokopedia.unifyprinciples.R.color.Unify_YN400)
                }?.let { ColorStateList.valueOf(it) }
            }
            else -> {
                childFragmentManager.beginTransaction().add(R.id.frameCouponList, TokomemberDashCouponFragment.newInstance(arguments, false), tag).addToBackStack(tag).commit()
                tvProgramStatus.backgroundTintList = context?.let {
                    ContextCompat.getColor(
                        it, com.tokopedia.unifyprinciples.R.color.Unify_NN400)
                }?.let { ColorStateList.valueOf(it) }
            }
        }
        tvTransactionCount.text = membershipGetProgramForm?.programForm?.analytics?.trxCount
        tvIncome.text = membershipGetProgramForm?.programForm?.analytics?.totalIncome
        tvTransactionPremium.text = "Rp" + CurrencyFormatHelper.convertToRupiah(membershipGetProgramForm?.programForm?.tierLevels?.firstOrNull()?.threshold.toString())
        tvMemberCountPremium.text = membershipGetProgramForm?.levelInfo?.levelList?.firstOrNull()?.totalMember
        tvTransactionVip.text = "Rp" + CurrencyFormatHelper.convertToRupiah(membershipGetProgramForm?.programForm?.tierLevels?.getOrNull(1)?.threshold.toString())
        tvMemberCountVip.text = membershipGetProgramForm?.levelInfo?.levelList?.getOrNull(1)?.totalMember

    }

    private fun setHeader() {
        header_program_detail.title = "Detail Program"
        header_program_detail.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun getDateTimeSpannable(dateTimeString:String?,startDateLen:Int,endDateLen:Int) : SpannableString{
      val ss = SpannableString(dateTimeString)
        val delimiterIdx = ss.indexOf("-")
        ss.setSpan(
            StyleSpan(Typeface.BOLD),
            0,startDateLen,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        ss.setSpan(
            StyleSpan(Typeface.BOLD),
            delimiterIdx+2,delimiterIdx+2+endDateLen,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return ss
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    companion object {
        fun newInstance(extras: Bundle?) = TokomemberDashProgramDetailFragment().apply {
            arguments = extras
        }
    }
}
