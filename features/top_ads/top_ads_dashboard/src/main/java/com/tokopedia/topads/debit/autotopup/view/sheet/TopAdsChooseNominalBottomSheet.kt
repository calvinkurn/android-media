package com.tokopedia.topads.debit.autotopup.view.sheet

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.util.Utils.convertToCurrency
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.CreditResponse
import com.tokopedia.topads.dashboard.data.utils.ListUnifyUtils.getShownRadioButton
import com.tokopedia.topads.dashboard.data.utils.ListUnifyUtils.setSelectedItem
import com.tokopedia.topads.dashboard.data.utils.Utils.calculatePercentage
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsEditAutoTopUpActivity
import com.tokopedia.topads.debit.autotopup.view.viewmodel.TopAdsAutoTopUpViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.topads_dash_topup_nominal_sheet.*
import javax.inject.Inject

/**
 * Created by Pika on 7/10/20.
 */

class TopAdsChooseNominalBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var creditData: CreditResponse? = null
    private var bonus = 1.0
    private var contentView: View? = null
    private var isTopUp = false
    var onSaved: ((positionSelected: Int) -> Unit)? = null
    var onSavedAutoTopUp: ((positionSelected: Int) -> Unit)? = null
    private var defPosition = 0
    private var topUpChoice = 2
    private var autoTopUpData: AutoTopUpStatus? = null

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TopAdsAutoTopUpViewModel::class.java)
    }

    companion object {
        private const val TOPADS_BOTTOM_SHEET_TAG = "CHOOSE_AUTO_TOPUP_NOMINAL"
        fun newInstance() = TopAdsChooseNominalBottomSheet()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initInjector()
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initInjector() {
        DaggerTopAdsDashboardComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.topads_dash_topup_nominal_sheet, null)
        showCloseIcon = false
        clearContentPadding = true
        isDragable = true
        setChild(contentView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAutoTopUpStatusFull()
        view.run {
            if (isTopUp)
                setList()
        }
        bottomSheetBehaviorKnob(view, true)
        saveButton?.setOnClickListener {
            dismiss()
            if (isTopUp && creditData?.credit?.isNotEmpty() == true)
                onSaved?.invoke(topUpChoice)
            else if (!isTopUp && autoTopUpData != null && autoTopUpData?.availableNominals?.isNotEmpty() == true)
                onSavedAutoTopUp?.invoke(defPosition)
        }
        if (isTopUp) {
            saveButton?.buttonType = UnifyButton.Type.TRANSACTION
            saveButton?.buttonVariant = UnifyButton.Variant.FILLED
            saveButton?.text = resources.getString(R.string.label_add_credit)
        } else {
            bonusTxt.visibility = View.VISIBLE
        }
        viewModel.getAutoTopUpStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetAutoTopUp(it.data)
            }
        })
    }

    private fun setAutoTopUpList() {
        val listUnify = ArrayList<ListItemUnify>()
        autoTopUpData?.availableNominals?.forEachIndexed { index, it ->
            if (defPosition == it.id)
                defPosition = index
            val list = ListItemUnify(it.priceFmt, "")
            list.isBold = true
            list.setVariant(rightComponent = ListItemUnify.RADIO_BUTTON)
            listUnify.add(list)
        }
        if (defPosition >= autoTopUpData?.availableNominals?.size ?: 0)
            defPosition = 0
        listGroup?.setData(listUnify)
        setBonus(defPosition)
        listGroup?.run {
            onLoadFinish {
                this.setOnItemClickListener { _, _, position, _ ->
                    defPosition = position
                    selectOperation(this, listUnify, position)
                }
                listUnify.forEachIndexed { position, it ->
                    it.listRightRadiobtn?.setOnClickListener {
                        defPosition = position
                        selectOperation(this, listUnify, position)
                    }
                }
                if (autoTopUpData?.availableNominals?.size != 0)
                    selectOperation(this, listUnify, defPosition)
            }
        }
    }

    private fun selectOperation(list1: ListUnify, list: ArrayList<ListItemUnify>, position: Int) {
        list1.run {
            val selectedItem = this.getItemAtPosition(position) as ListItemUnify
            list.filter { it.getShownRadioButton()?.isChecked ?: false }
                    .filterNot { it == selectedItem }
                    .onEach { it.getShownRadioButton()?.isChecked = false }
            selectedItem.getShownRadioButton()?.isChecked = true
            setBonus(position)
        }
    }

    private fun select(list1: ListUnify, list: ArrayList<ListItemUnify>, position: Int) {
        list1.setSelectedItem(list, position) {
        }
    }

    private fun onSuccessGetAutoTopUp(data: AutoTopUpStatus) {
        autoTopUpData = data
        bonus = data.statusBonus
        status_title.text = String.format(getString(R.string.topads_auto_topup_widget, bonus))
        val isAutoTopUpActive = (data.status.toIntOrZero()) != TopAdsDashboardConstant.AUTO_TOPUP_INACTIVE
        if (!isAutoTopUpActive && isTopUp) {
            showAutoAdsOption()
        }
        if (!isTopUp)
            setAutoTopUpList()
    }

    private fun setList() {
        val listUnify = ArrayList<ListItemUnify>()
        creditData?.credit?.forEach {
            val list = ListItemUnify(it.productPrice, "")
            list.isBold = true
            list.setVariant(rightComponent = ListItemUnify.RADIO_BUTTON)
            listUnify.add(list)
        }
        listGroup?.setData(listUnify)
        listGroup?.run {
            onLoadFinish {
                this.setOnItemClickListener { _, _, position, _ ->
                    topUpChoice = position
                    select(this, listUnify, position)

                }
                listUnify.forEachIndexed { position, it ->
                    it.listRightRadiobtn?.setOnClickListener {
                        topUpChoice = position
                        select(this, listUnify, position)
                    }
                }
                if (topUpChoice < listUnify.size)
                    select(this, listUnify, topUpChoice)
            }
        }
    }

    private fun showAutoAdsOption() {
        suggestAutoTopUp.visibility = View.VISIBLE
        onBoarding?.setOnClickListener {
            startActivity(Intent(context, TopAdsEditAutoTopUpActivity::class.java))
        }
    }

    private fun setBonus(position: Int) {
        context?.let {
            bonusTxt.text = Html.fromHtml(String.format(it.getString(R.string.topads_dash_bonus_String_bottomsheet), "$bonus%",
                    convertToCurrency(calculatePercentage((autoTopUpData?.availableNominals?.get(position)?.priceFmt
                            ?: "").removeCommaRawString(), bonus).toLong())))

        }
    }

    fun show(fragmentManager: FragmentManager,
             data: CreditResponse?, isTopUp: Boolean, position: Int) {
        this.creditData = data
        this.isTopUp = isTopUp
        this.defPosition = position
        show(fragmentManager, TOPADS_BOTTOM_SHEET_TAG)
    }
}