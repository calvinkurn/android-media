package com.tokopedia.addon.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.addon.di.DaggerAddOnComponent
import com.tokopedia.addon.presentation.adapter.AddOnAdapter
import com.tokopedia.addon.presentation.listener.AddOnComponentListener
import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel
import com.tokopedia.addon.presentation.uimodel.AddOnMapper
import com.tokopedia.addon.presentation.uimodel.AddOnParam
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.addon.presentation.viewmodel.AddOnViewModel
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product_service_widget.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AddOnWidgetView : BaseCustomView {

    @Inject
    lateinit var viewModel: AddOnViewModel
    private var addonAdapter: AddOnAdapter = AddOnAdapter(::onAddonClickListener,
        ::onHelpClickListener, ::onItemImpressionListener)
    private var tfTitle: Typography? = null
    private var llSeeAll: LinearLayoutCompat? = null
    private var divSeeAll: View? = null
    private var listener: AddOnComponentListener? = null
    private var isObserverSetupDone: Boolean = false

    constructor(context: Context) : super(context) {
        setup(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup(context, attrs)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val lifecycleOwner = context as? LifecycleOwner ?:
            (context as? ContextThemeWrapper)?.baseContext as? LifecycleOwner
        lifecycleOwner?.run {
            if (isObserverSetupDone) return@run
            isObserverSetupDone = true
            viewModel.errorThrowable.observe(this) {
                listener?.onAddonComponentError(ErrorHandler.getErrorMessage(context, it))
            }
            viewModel.getAddOnResult.observe(this) {
                addonAdapter.setItems(it)
                viewModel.lastSelectedAddOn = AddOnMapper.getGroupSelectedAddons(it).toMutableList()
                viewModel.setSelectedAddons(it)
            }
            viewModel.isAddonDataEmpty.observe(this) {
                if (it) listener?.onDataEmpty()
                this@AddOnWidgetView.isVisible = !it
            }
            viewModel.shouldShowSeeAll.observe(this) {
                llSeeAll?.isVisible = it
                divSeeAll?.isVisible = it
            }
            viewModel.totalPrice.observe(this) {
                listener?.onTotalPriceCalculated(it)
            }
            viewModel.aggregatedData.observe(this) {
                listener?.onAggregatedDataObtained(it)
            }
            viewModel.saveSelectionResult.observe(this) {
                when (it) {
                    is Fail -> {
                        listener?.onSaveAddonFailed(
                            ErrorHandler.getErrorMessage(context, it.throwable))
                        viewModel.restoreSelection()
                    }
                    is Success -> {
                        if (it.data.isNotEmpty()) {
                            val addonGroups = AddOnMapper.deepCopyAddonGroup(it.data)
                            viewModel.preselectedAddonIds = AddOnMapper.getSelectedAddonsIds(addonGroups)
                            listener?.onSaveAddonSuccess(
                                viewModel.preselectedAddonIds,
                                AddOnMapper.flatmapToChangedAddonSelection(addonGroups),
                                addonGroups
                            )
                            viewModel.lastSelectedAddOnGroups = addonGroups
                        } else {
                            listener?.onSaveAddonLoading()
                        }
                    }
                }
            }
            viewModel.autoSave.observe(this) {
                if (it.isActive)
                    viewModel.saveAddOnState(it.cartId, it.atcSource)
            }
        }
    }

    private fun setup(context: Context, attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.customview_addon_widget, this)
        val rvAddons: RecyclerView = view.findViewById(R.id.rv_addons)
        tfTitle = view.findViewById(R.id.tf_widget_title)
        llSeeAll = view.findViewById(R.id.ll_see_all)
        divSeeAll = view.findViewById(R.id.div_see_all)
        setupItems(rvAddons)
        defineCustomAttributes(attrs)
        initInjector()
        setupSeeAll()
    }

    private fun setupSeeAll() {
        llSeeAll?.setOnClickListener {
            llSeeAll?.gone()
            divSeeAll?.gone()
            viewModel.desimplifyAddonList()
        }
    }

    private fun setupItems(rvBundles: RecyclerView) {
        rvBundles.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = addonAdapter
        }
    }

    private fun initInjector() {
        DaggerAddOnComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.AddOnWidgetView, 0, 0)

            try {
                val text = styledAttributes.getString(R.styleable.AddOnWidgetView_addonwidget_title).orEmpty()
                val isShowDescription = styledAttributes.getBoolean(R.styleable.AddOnWidgetView_addonwidget_show_description, false)
                addonAdapter.showDescription(isShowDescription)
                setTitleText(text)
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun onAddonClickListener(
        index: Int,
        indexChild: Int,
        addOnGroupUIModels: List<AddOnGroupUIModel>
    ) {
        val addonChild = addOnGroupUIModels.getOrNull(index)?.addon?.getOrNull(indexChild) ?: return
        viewModel.lastSelectedAddOn.addOrUpdate(index, addonChild)
        listener?.onAddonComponentClick(index, indexChild, addOnGroupUIModels)
        viewModel.setSelectedAddons(addOnGroupUIModels)
    }

    private fun <E> MutableList<E>.addOrUpdate(index: Int, addonChild: E) {
        if (getOrNull(index) == null) {
            add(index, addonChild)
        } else {
            set(index, addonChild)
        }
    }

    private fun onHelpClickListener(index: Int, indexChild: Int, addonGroups: List<AddOnGroupUIModel>, addonSelected: AddOnUIModel) {
        listener?.onAddonHelpClick(index, indexChild, addonGroups)
        RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=${addonSelected.eduLink}")
    }

    private fun onItemImpressionListener(index: Int, indexChild: Int, addonGroups: List<AddOnGroupUIModel>) {
        listener?.onAddOnItemImpression(index, indexChild, addonGroups)
    }

    fun setTitleText(text: String) {
        tfTitle?.setTextAndCheckShow(text)
    }

    fun setListener(listener: AddOnComponentListener) {
        this.listener = listener
    }

    fun getAddonData(
        addOnParam: AddOnParam,
        isSimplified: Boolean = false
    ) {
        viewModel.getAddOn(addOnParam, isSimplified)
    }

    fun setSelectedAddons(selectedAddonIds: List<String>) {
        viewModel.setPreselectedAddOn(selectedAddonIds)
    }

    fun saveAddOnState(cartId: Long, source: String) {
        viewModel.saveAddOnState(cartId, source)
    }

    fun getAddOnAggregatedData(
        addOnIds: List<String>,
        addOnTypes: List<String>,
        addOnWidgetParam: AddOnParam
    ) {
        viewModel.getAddOnAggregatedData(addOnIds, addOnTypes, addOnWidgetParam)
    }

    fun setAutosaveAddon(cartId: Long, atcSource: String) {
        viewModel.setAutosave(cartId, atcSource)
    }

    fun getFetchedAddon(): List<AddOnGroupUIModel> {
        return viewModel.getAddOnResult.value.orEmpty()
    }
}
