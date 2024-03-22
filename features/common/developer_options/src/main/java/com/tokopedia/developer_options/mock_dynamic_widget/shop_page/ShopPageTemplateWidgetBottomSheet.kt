package com.tokopedia.developer_options.mock_dynamic_widget.shop_page

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.developer_options.R
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ShopPageTemplateWidgetBottomSheet : BottomSheetUnify(), ShopPageMockWidgetAdapter.ShopPageMockWidgetViewHolder.Listener, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val childLayoutRes = R.layout.shop_page_template_widget_bottom_sheet_layout
    private val adapter by lazy {
        ShopPageMockWidgetAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
    }

    private fun setDefaultParams() {
        setTitle(TITLE)
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configTextFieldSearchWidgetName()
        configShopPageMockWidgetOption()
        configToggleFestivity()
    }

    private fun configTextFieldSearchWidgetName() {
        view?.findViewById<TextFieldUnify>(R.id.text_field_search_widget_name)?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.let {
                    adapter.filterWidgetByName(it)
                    val rv = view?.findViewById<RecyclerView>(R.id.rv_mock_shop_widget_option)
                    rv?.scrollToPosition(0)
                }
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) { /* no need to implement */
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) { /* no need to implement */
            }
        })
    }

    private fun configToggleFestivity() {
        view?.findViewById<CheckboxUnify>(R.id.toggle_is_festivity)?.setOnCheckedChangeListener { _, isChecked ->
            adapter.updateIsFestivity(isChecked)
        }
    }

    private fun configShopPageMockWidgetOption() {
        launch {
            val listShopPageMockWidgetOption = generateTemplateShopWidgetOption()
            val rv = view?.findViewById<RecyclerView>(R.id.rv_mock_shop_widget_option)
            rv?.adapter = adapter
            adapter.setListShopPageMockWidget(listShopPageMockWidgetOption)
        }
    }

    private fun generateTemplateShopWidgetOption(): List<ShopPageMockWidgetModel> {
        return context?.resources?.let {
            val shopPageMockJsonData = ShopPageMockWidgetModelMapper.getShopPageMockJsonFromRaw(it)
            return ShopPageMockWidgetModelMapper.generateTemplateShopWidgetData(shopPageMockJsonData)
        } ?: listOf()
    }

    fun setOnOptionSelected(onOptionSelected: (ShopPageMockWidgetModel) -> Unit) {
        this.onOptionSelected = onOptionSelected
    }

    private var onOptionSelected: (ShopPageMockWidgetModel) -> Unit = {}

    companion object {
        const val TITLE = "Choose Widget Name From Template"
        fun createInstance(): ShopPageTemplateWidgetBottomSheet {
            return ShopPageTemplateWidgetBottomSheet()
        }
    }

    override fun onMockWidgetItemClick(shopPageMockWidgetModel: ShopPageMockWidgetModel) {
        onOptionSelected.invoke(shopPageMockWidgetModel)
        dismiss()
    }

}
