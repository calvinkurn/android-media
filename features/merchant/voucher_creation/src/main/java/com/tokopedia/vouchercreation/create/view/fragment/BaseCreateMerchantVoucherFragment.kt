package com.tokopedia.vouchercreation.create.view.fragment

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.VoucherBottomView
import com.tokopedia.vouchercreation.create.view.enums.CreateVoucherBottomSheetType
import com.tokopedia.vouchercreation.create.view.typefactory.CreateVoucherTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.NextButtonUiModel

abstract class BaseCreateMerchantVoucherFragment<F : CreateVoucherTypeFactory, WTF : BaseAdapterTypeFactory>(private val onNext: () -> Unit) : BaseListFragment<Visitable<CreateVoucherTypeFactory>, WTF>() {

    private var activeBottomSheetType: CreateVoucherBottomSheetType = CreateVoucherBottomSheetType.CREATE_PROMO_CODE

    private val nextButtonWidget by lazy {
        NextButtonUiModel(onNext)
    }

    private val widgetList : MutableList<Visitable<CreateVoucherTypeFactory>> = mutableListOf()

    private var bottomSheetViewArray: SparseArray<BottomSheetUnify> = SparseArray()

    open var extraWidget : List<Visitable<F>> = mutableListOf()

    abstract fun onDismissBottomSheet(bottomSheetType: CreateVoucherBottomSheetType)

    abstract fun onBeforeShowBottomSheet(bottomSheetType: CreateVoucherBottomSheetType)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    open fun setupView() {
        if (isAdded) {
            widgetList.run {
                clear()
                extraWidget.asListOfType<Visitable<CreateVoucherTypeFactory>>()?.let {
                    addAll(it)
                }
                add(nextButtonWidget)
                adapter?.data?.clear()
                renderList(this)
            }
        }
    }

    fun dismissBottomSheet() {
        val activeBottomSheet = childFragmentManager.findFragmentByTag(activeBottomSheetType.tag)
        (activeBottomSheet as? BottomSheetUnify)?.dismiss()
    }

    protected fun showBottomSheet(bottomSheetType: CreateVoucherBottomSheetType) {
        activeBottomSheetType = bottomSheetType
        bottomSheetViewArray.get(bottomSheetType.key)?.run {
            (this as? VoucherBottomView)?.bottomSheetViewTitle?.let { bottomSheetTitle ->
                setTitle(bottomSheetTitle)
            }
            setOnDismissListener {
                dismissBottomView()
            }
            setCloseClickListener {
                dismissBottomView()
            }
            onBeforeShowBottomSheet(bottomSheetType)
            show(this@BaseCreateMerchantVoucherFragment.childFragmentManager, bottomSheetType.tag)
        }
    }

    protected fun addBottomSheetView (bottomSheetType: CreateVoucherBottomSheetType, bottomSheetFragment: BottomSheetUnify) {
        bottomSheetViewArray.put(bottomSheetType.key, bottomSheetFragment)
    }

    private fun dismissBottomView() {
        activity?.let {
            KeyboardHandler.hideSoftKeyboard(it)
        }
        onDismissBottomSheet(activeBottomSheetType)
    }

    private inline fun <reified T> List<*>.asListOfType(): List<T>? =
            if (all { it is T })
                @Suppress("UNCHECKED_CAST")
                this as List<T> else
                null

}