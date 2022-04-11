package com.tokopedia.tokofood.feature.ordertracking.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.OrderDetailSecondaryActionButtonBottomsheetBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.SecondaryActionButtonAdapter
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.itemdecoration.SecondaryActionButtonItemDecoration
import com.tokopedia.tokofood.feature.ordertracking.presentation.navigator.OrderTrackingNavigator
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ActionButtonsUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class SecondaryActionBottomSheet: BottomSheetUnify(), SecondaryActionButtonAdapter.ActionButtonListener {

    private val secondaryButtonAdapter by lazy {
        SecondaryActionButtonAdapter(this)
    }

    private var binding by autoClearedNullable<OrderDetailSecondaryActionButtonBottomsheetBinding>()

    private var navigator: OrderTrackingNavigator? = null

    private var actionButtonList: List<ActionButtonsUiModel.ActionButton>? = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetCloseIcon()
        setTitle(getString(R.string.secondary_action_bottomsheet_header))
        setSecondaryActionAdapter()
    }

    override fun onActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        navigator?.goToHelpPage(button.appUrl)
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    fun dismissBottomSheet() {
        if (isVisible) {
            dismiss()
        }
    }

    fun setActionBtnList(actionButtonList: List<ActionButtonsUiModel.ActionButton>) {
        this.actionButtonList = actionButtonList
    }

    private fun setChildView(inflater: LayoutInflater, container: ViewGroup?) {
        val view = inflater.inflate(R.layout.order_detail_secondary_action_button_bottomsheet, container, false)
        binding = OrderDetailSecondaryActionButtonBottomsheetBinding.bind(view)
        setChild(view)
    }

    private fun setBottomSheetCloseIcon() {
        bottomSheetClose.run {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bottomsheet_close_secondary_action))
            layoutParams.apply {
                width = context.resources.getDimension(com.tokopedia.unifycomponents.R.dimen.spacing_lvl5).toInt()
                height = context.resources.getDimension(com.tokopedia.unifycomponents.R.dimen.spacing_lvl5).toInt()
            }
        }
    }

    private fun setSecondaryActionAdapter() {
        binding?.rvSecondaryActionButton?.run {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SecondaryActionButtonItemDecoration(context))
            adapter = secondaryButtonAdapter
        }
        secondaryButtonAdapter.setActionButtonList(actionButtonList.orEmpty())
    }

    companion object {
        val TAG: String = SecondaryActionBottomSheet::class.java.simpleName
    }
}