package com.tokopedia.vouchercreation.create.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.create.view.bottomsheet.CreateVoucherBottomSheetType
import com.tokopedia.vouchercreation.create.view.customview.bottomsheet.VoucherBottomView
import com.tokopedia.vouchercreation.create.view.typefactory.CreateVoucherTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.NextButtonUiModel

abstract class BaseCreateMerchantVoucherFragment<F : CreateVoucherTypeFactory, WTF : BaseAdapterTypeFactory>(val onNext: () -> Unit) : BaseListFragment<Visitable<CreateVoucherTypeFactory>, WTF>() {

    private val bottomSheet: BottomSheetUnify by lazy {
        BottomSheetUnify().apply {
            setOnDismissListener {
                dismissBottomView()
            }
            setCloseClickListener {
                dismissBottomView()
                this.dismiss()
            }
        }
    }

    private var activeBottomSheetType: CreateVoucherBottomSheetType = CreateVoucherBottomSheetType.CREATE_PROMO_CODE

    private val nextButtonWidget by lazy {
        NextButtonUiModel(onNext)
    }

    private val widgetList : MutableList<Visitable<CreateVoucherTypeFactory>> = mutableListOf()

    private var bottomSheetViewArray: SparseArray<View> = SparseArray()

    open var extraWidget : List<Visitable<F>> = mutableListOf()

    abstract fun onDismissBottomSheet(bottomSheetType: CreateVoucherBottomSheetType)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    open fun setupView() {
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

    protected fun showBottomSheet(bottomSheetType: CreateVoucherBottomSheetType) {
        activeBottomSheetType = bottomSheetType
        bottomSheetViewArray.get(bottomSheetType.key)?.let { bottomSheetView ->
            bottomSheet.run {
                (bottomSheetView as? VoucherBottomView)?.title?.let { title ->
                    setTitle(title)
                }
                setChild(bottomSheetView)
                show(this@BaseCreateMerchantVoucherFragment.childFragmentManager, bottomSheetType.tag)
            }
        }
    }

    protected fun addBottomSheetView (bottomSheetType: CreateVoucherBottomSheetType, bottomSheetView: View) {
        bottomSheetViewArray.put(bottomSheetType.key, bottomSheetView)
    }

    private fun dismissBottomView() {
        hideKeyboard()
        onDismissBottomSheet(activeBottomSheetType)
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private inline fun <reified T> List<*>.asListOfType(): List<T>? =
            if (all { it is T })
                @Suppress("UNCHECKED_CAST")
                this as List<T> else
                null

}