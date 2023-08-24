package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.R
import com.tokopedia.recharge_component.databinding.WidgetRechargeCheckBalanceBinding
import com.tokopedia.recharge_component.model.check_balance.RechargeCheckBalanceUnitModel
import com.tokopedia.recharge_component.presentation.adapter.RechargeCheckBalanceUnitAdapter
import com.tokopedia.recharge_component.presentation.adapter.viewholder.RechargeCheckBalanceUnitViewHolder
import com.tokopedia.recharge_component.presentation.util.CustomDividerItemDecorator
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.toDp
import org.jetbrains.annotations.NotNull

class RechargeCheckBalanceWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = WidgetRechargeCheckBalanceBinding.inflate(LayoutInflater.from(context), this, true)
    private var checkBalanceAdapter: RechargeCheckBalanceUnitAdapter? = null
    private var checkBalanceWidgetListener: RechargeCheckBalanceWidgetListener? = null

    init {
        binding.root.setOnClickListener {
            checkBalanceWidgetListener?.onClickWidget()
        }
    }

    fun setListener(listener: RechargeCheckBalanceWidgetListener) {
        checkBalanceWidgetListener = listener
    }

    fun setBalanceInfo(balanceInfo: List<RechargeCheckBalanceUnitModel>) {
        binding.run {
            checkBalanceAdapter = RechargeCheckBalanceUnitAdapter().apply {
                setBalanceInfo(balanceInfo)
                setCheckBalanceUnitListener(
                    object : RechargeCheckBalanceUnitViewHolder.RechargeCheckBalanceUnitListener {
                        override fun onClickCheckBalanceUnit() {
                            checkBalanceWidgetListener?.onClickWidget()
                        }
                    }
                )
            }
            val dividerDrawable = ContextCompat.getDrawable(
                context,
                R.drawable.divider_client_number_check_balance
            )
            val layoutManager = GridLayoutManager(context, balanceInfo.size)
            checkBalanceRv.layoutManager = layoutManager
            checkBalanceRv.adapter = checkBalanceAdapter
            checkBalanceRv.addItemDecoration(
                CustomDividerItemDecorator(
                    context,
                    DividerItemDecoration.HORIZONTAL,
                    dividerDrawable
                )
            )
        }
    }

    fun showWarningMessage(message: String) {
        showWidgetMessage(message)
        binding.run {
            checkBalanceWarningIcon.run {
                setImage(
                    IconUnify.WARNING,
                    MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_YN500
                    )
                )
                show()
            }
            checkBalanceWarningContainer.background = MethodChecker.getDrawable(
                context,
                R.drawable.bg_client_number_check_balance_warning_shadow
            )
            checkBalanceWarningTxt.setTextColor(
                MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_YN500
                )
            )
        }
    }

    fun showCriticalMessage(message: String) {
        showWidgetMessage(message)
        binding.run {
            checkBalanceWarningIcon.run {
                setImage(
                    IconUnify.WARNING,
                    MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_RN500
                    )
                )
                show()
            }
            checkBalanceWarningContainer.background = MethodChecker.getDrawable(
                context,
                R.drawable.bg_client_number_check_balance_error_shadow
            )
            checkBalanceWarningTxt.setTextColor(
                MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_RN500
                )
            )
        }
    }

    fun showInformationMessage(message: String) {
        showWidgetMessage(message)
        binding.run {
            checkBalanceWarningIcon.hide()
            checkBalanceWarningContainer.background = MethodChecker.getDrawable(
                context,
                R.drawable.bg_client_number_check_balance_information_shadow
            )
            checkBalanceWarningTxt.setTextColor(
                MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN600
                )
            )
        }
    }

    private fun showWidgetMessage(message: String) {
        binding.run {
            checkBalanceWarningContainer.show()
            checkBalanceWarningTxt.text = MethodChecker.fromHtml(message)
            checkBalanceWarningContainer.setOnClickListener {
                checkBalanceWidgetListener?.onClickWidget()
            }
        }
    }

    fun showShimmering() {
        binding.run {
            checkBalanceRv.hide()
            checkBalanceWarningContainer.hide()
            checkBalanceShimmering.root.show()
        }
    }

    fun hideShimmering() {
        binding.run {
            checkBalanceRv.show()
            checkBalanceWarningContainer.show()
            checkBalanceShimmering.root.hide()
        }
    }

    fun hideWidgetMessage() {
        binding.checkBalanceWarningContainer.hide()
    }

    fun showLocalLoad(onClick: () -> Unit) {
        binding.run {
            checkBalanceRv.hide()
            checkBalanceWarningContainer.hide()
            checkBalanceLocalload.setOnClickListener { onClick.invoke() }
            checkBalanceLocalload.show()
        }
    }

    fun hideLocalLoad() {
        binding.run {
            checkBalanceRv.show()
            checkBalanceWarningContainer.show()
            checkBalanceLocalload.setOnClickListener { }
            checkBalanceLocalload.hide()
        }
    }

    fun showCheckBalanceRV() {
        binding.checkBalanceRv.show()
    }

    fun hideCheckBalanceRV() {
        binding.checkBalanceRv.hide()
    }

    fun setWarningContainerMargin(
        marginStart: Int = DEFAULT_WARNING_CONTAINER_MARGIN_0,
        marginTop: Int = DEFAULT_WARNING_CONTAINER_MARGIN_12,
        marginEnd: Int = DEFAULT_WARNING_CONTAINER_MARGIN_0,
        marginBottom: Int = DEFAULT_WARNING_CONTAINER_MARGIN_0
    ) {
        binding.checkBalanceWarningContainer.setMargin(
            marginStart.toDp(),
            marginTop.toDp(),
            marginEnd.toDp(),
            marginBottom.toDp()
        )
    }

    interface RechargeCheckBalanceWidgetListener {
        fun onClickWidget()
    }

    companion object {
        private const val DEFAULT_WARNING_CONTAINER_MARGIN_12 = 12
        private const val DEFAULT_WARNING_CONTAINER_MARGIN_0 = 0
    }
}
