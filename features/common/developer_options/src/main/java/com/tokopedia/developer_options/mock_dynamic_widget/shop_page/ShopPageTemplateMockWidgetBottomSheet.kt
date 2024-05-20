package com.tokopedia.developer_options.mock_dynamic_widget.shop_page

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.mock_dynamic_widget.shop_page.ShopPageMockWidgetModel.*
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ShopPageTemplateMockWidgetBottomSheet : BottomSheetUnify(), ShopPageMockWidgetAdapter.ShopPageMockWidgetViewHolder.Listener, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var onAddSelectedWidget: (List<ShopPageMockWidgetModel>, String) -> Unit = { _, _ -> }
    private val childLayoutRes = R.layout.shop_page_template_widget_bottom_sheet_layout
    private val adapterListShopWidget by lazy {
        ShopPageMockWidgetAdapter(this)
    }

    private val adapterSelectedListShopWidget by lazy {
        ShopPageMockWidgetAdapter()
    }

    private val isMockLottieUrlChecked: Boolean
        get() = view?.findViewById<CheckboxUnify>(R.id.toggle_mock_lottie_animation)?.isChecked.orFalse()
    private val lottieUrl: String
        get() = view?.findViewById<TextFieldUnify>(R.id.text_field_lottie_url)?.textFieldInput?.text?.toString().orEmpty()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
    }

    private fun setDefaultParams() {
        setTitle(TITLE)
        isHideable = false
        showCloseIcon = true
        showHeader = true
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
        configToggleFestivity()
        configListShopWidget()
        configSelectedShopWidget()
        configButtonAddSelectedShopWidget()
        configButtonClearSelectedShopWidget()
    }

    private fun configButtonAddSelectedShopWidget() {
        view?.findViewById<UnifyButton>(R.id.btn_add_selected_shop_widget)?.setOnClickListener {
            val listSelectedData = adapterSelectedListShopWidget.getData()
            if (listSelectedData.isEmpty()) {
                Toaster.build(it.rootView, "Selected shop widget is empty").show()
            } else if (isMockLottieUrlChecked && lottieUrl.isEmpty()) {
                Toaster.build(it.rootView, "Please enter the lottie url").show()
                return@setOnClickListener
            } else {
                onAddSelectedWidget.invoke(listSelectedData, lottieUrl)
                dismiss()
            }
        }
    }

    private fun configButtonClearSelectedShopWidget() {
        view?.findViewById<UnifyButton>(R.id.btn_clear_selected_shop_widget)?.setOnClickListener {
            adapterSelectedListShopWidget.clear()
        }
    }

    private fun configTextFieldSearchWidgetName() {
        view?.findViewById<TextFieldUnify>(R.id.text_field_search_widget_name)?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.let {
                    adapterListShopWidget.filterWidgetByName(it)
                    val rv = view?.findViewById<RecyclerView>(R.id.rv_list_mock_shop_widget)
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
            adapterListShopWidget.updateIsFestivity(isChecked)
            configToggleLottieAnimation(isChecked)
        }
    }

    private fun configToggleLottieAnimation(isOptionShown: Boolean) {
        val toggleMockLottieAnimation = view?.findViewById<CheckboxUnify>(R.id.toggle_mock_lottie_animation)
        val textFieldLottieUrl = view?.findViewById<TextFieldUnify>(R.id.text_field_lottie_url)

        toggleMockLottieAnimation?.apply {
            isVisible = isOptionShown
            setOnCheckedChangeListener { _, isChecked ->
                textFieldLottieUrl?.isVisible = isChecked
            }
        }
    }

    private fun configListShopWidget() {
        launch {
            val listShopPageMockWidgetOption = generateTemplateShopWidgetOption()
            val rv = view?.findViewById<RecyclerView>(R.id.rv_list_mock_shop_widget)
            rv?.adapter = adapterListShopWidget
            adapterListShopWidget.setListShopPageMockWidget(listShopPageMockWidgetOption)
        }
    }

    private fun configSelectedShopWidget() {
        val rv = view?.findViewById<RecyclerView>(R.id.rv_selected_mock_shop_widget)
        rv?.adapter = adapterSelectedListShopWidget
    }


    private fun generateTemplateShopWidgetOption(): List<ShopPageMockWidgetModel> {
        return context?.resources?.let {
            val shopPageMockJsonData = ShopPageMockWidgetModelMapper.getShopPageMockJsonFromRaw(it)
            return ShopPageMockWidgetModelMapper.generateTemplateShopWidgetData(shopPageMockJsonData)
        } ?: listOf()
    }

    companion object {
        const val TITLE = "Select Template Shop Widget"
        fun createInstance(): ShopPageTemplateMockWidgetBottomSheet {
            return ShopPageTemplateMockWidgetBottomSheet()
        }
    }

    override fun onMockWidgetItemClick(shopPageMockWidgetModel: ShopPageMockWidgetModel) {
        adapterSelectedListShopWidget.addMockWidgetModel(listOf(shopPageMockWidgetModel))
    }

    override fun onClearMockWidgetItemClick(shopPageMockWidgetModel: ShopPageMockWidgetModel) {}

    fun setOnAddSelectedShopWidget(onAddSelectedWidget: (List<ShopPageMockWidgetModel>, String) -> Unit) {
        this.onAddSelectedWidget = onAddSelectedWidget
    }

}
