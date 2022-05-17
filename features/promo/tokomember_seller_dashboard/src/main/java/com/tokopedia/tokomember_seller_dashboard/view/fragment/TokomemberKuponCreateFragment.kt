package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.tokomember_common_widget.callbacks.ChipGroupCallback
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.view.customview.TmSingleCouponView
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashCreateViewModel
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.tm_dash_kupon_create.*
import kotlinx.android.synthetic.main.tm_dash_kupon_create_container.*
import kotlinx.android.synthetic.main.tm_dash_progrm_form.*
import kotlinx.android.synthetic.main.tm_dash_single_coupon.*
import javax.inject.Inject

class TokomemberKuponCreateFragment : BaseDaggerFragment() {

    private var selectedChipPositionDate: Int = 0
    private var selectedChipPositionLevel: Int = 0
    private var selectedChipPositionKupon: Int = 0
    private var selectedChipPositionCashback: Int = 0

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tokomemberDashCreateViewModel: TokomemberDashCreateViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberDashCreateViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_kupon_create_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderHeader()
        renderSingleCoupon()
        renderMultipleCoupon()
        renderProgram()
        renderButton()
        observeViewModel()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().build().inject(this)
    }

    private fun observeViewModel() {

    }

    private fun renderHeader() {

        headerKupon?.apply {
            title = "Buat Kupon"
            subtitle = "Langkah 3 dari 4"
            isShowBackButton = true
        }
        progressKupon?.apply {
            progressBarColorType = ProgressBarUnify.COLOR_GREEN
            progressBarHeight = ProgressBarUnify.SIZE_SMALL
            setValue(80, false)
        }
    }

    private fun renderButton() {

    }

    private fun renderMultipleCoupon() {
        val content = Typography(requireContext())
        content.apply {
            text = "kkk"
            setType(Typography.BODY_3)
            setWeight(Typography.REGULAR)
        }
        val toko = this.context?.let { TmSingleCouponView(it) }
        val itemAccordionPremium = toko?.let {
            AccordionDataUnify(
                title = "Untuk member Premium",
                expandableView = it,
                isExpanded = false,
                icon = context?.getDrawable(R.drawable.ic_tokomember_premium),
                subtitle = "Cashback Rp10.000"
            ).apply {
                borderBottom = true
                borderTop = true
            }
        }

        val itemAccordionVip = toko?.let {
            AccordionDataUnify(
                title = "Untuk member VIP",
                subtitle = "Cashback Rp10.000",
                expandableView = content,
                isExpanded = false,
                icon = context?.getDrawable(R.drawable.ic_tokomember_vip)
            ).apply {
                borderBottom = true
                borderTop = false
            }
        }

        if (itemAccordionPremium != null) {
            accordionUnifyPremium.addGroup(itemAccordionPremium)
        }
        if (itemAccordionVip != null) {
            accordionUnifyVIP.addGroup(itemAccordionVip)
        }
        accordionUnifyPremium.onItemClick = ::handleAccordionClickPremium
        accordionUnifyVIP.onItemClick = ::handleAccordionClickVip

    }

    private fun handleAccordionClickPremium(position:Int, expanded: Boolean){
        if (expanded){
            accordionUnifyPremium.expandGroup(position)
        } else {
            accordionUnifyPremium.collapseGroup(position)
        }
    }

    private fun handleAccordionClickVip(position:Int, expanded: Boolean){
        if (expanded){
            accordionUnifyVIP.expandGroup(position)
        } else {
            accordionUnifyVIP.collapseGroup(position)
        }
    }

    private fun renderProgram() {

        chipDateSelection.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionDate = position
            }
        })
        chipDateSelection.setDefaultSelection(selectedChipPositionDate)
        chipDateSelection.addChips(arrayListOf("Sesuai Periode Program", "Atur Manual"))

        textFieldProgramStartDate.setFirstIcon(R.drawable.tm_dash_calender)
        textFieldProgramStartTime.setFirstIcon(R.drawable.tm_dash_clock)
        textFieldProgramEndDate.setFirstIcon(R.drawable.tm_dash_calender)
        textFieldProgramEndTime.setFirstIcon(R.drawable.tm_dash_clock)
    }

    private fun renderSingleCoupon() {
        chipGroupLevel.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionLevel = position
            }
        })
        chipGroupLevel.setDefaultSelection(selectedChipPositionLevel)
        chipGroupLevel.addChips(arrayListOf("Premium", "VIP"))

        chipGroupKuponType.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionKupon = position
            }
        })
        chipGroupKuponType.setDefaultSelection(selectedChipPositionKupon)
        chipGroupKuponType.addChips(arrayListOf("Cashback", "Gratis Ongkir"))

        chipGroupCashbackType.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionCashback = position
            }
        })
        chipGroupCashbackType.setDefaultSelection(selectedChipPositionCashback)
        chipGroupCashbackType.addChips(arrayListOf("Rupiah(Rp)", "Persentase (%)"))

    }

    companion object {

        fun newInstance(): TokomemberKuponCreateFragment {
            return TokomemberKuponCreateFragment()
        }
    }
}