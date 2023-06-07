package com.tokopedia.addon.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.addon.di.DaggerAddOnComponent
import com.tokopedia.addon.presentation.adapter.AddOnAdapter
import com.tokopedia.addon.presentation.listener.AddOnComponentListener
import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.addon.presentation.viewmodel.AddOnViewModel
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalFintech
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.product_service_widget.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class AddOnWidgetView : BaseCustomView {

    @Inject
    lateinit var viewModel: AddOnViewModel
    private var addonAdapter: AddOnAdapter = AddOnAdapter(::onAddonClickListener, ::onHelpClickListener)
    private var tfTitle: Typography? = null
    private var listener: AddOnComponentListener? = null

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
        val lifecycleOwner = context as? LifecycleOwner
        lifecycleOwner?.run {
            viewModel.errorThrowable.observe(this) {
                listener?.onAddonComponentError(it)
            }
            viewModel.getAddOnResult.observe(this) {
                addonAdapter.setItems(it)
            }
            viewModel.isAddonDataEmpty.observe(this) {
                if (it) listener?.onDataEmpty()
                this@AddOnWidgetView.isVisible = !it
            }
            viewModel.getEduUrlResult.observe(this) {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalFintech.INSURANCE_INFO)
                intent.putExtra(
                    ApplinkConstInternalFintech.PARAM_INSURANCE_INFO_URL,
                    it
                )
                context.startActivity(intent)
            }
        }
    }

    private fun setup(context: Context, attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.customview_addon_widget, this)
        val rvAddons: RecyclerView = view.findViewById(R.id.rv_addons)
        tfTitle = view.findViewById(R.id.tf_widget_title)
        setupItems(rvAddons)
        defineCustomAttributes(attrs)
        initInjector()
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
        listener?.onAddonComponentClick(index, indexChild, addOnGroupUIModels)
    }

    private fun onHelpClickListener(position: Int, addOnUIModel: AddOnUIModel) {
        listener?.onAddonHelpClick(position, addOnUIModel)
        viewModel.getEduUrl(addOnUIModel)
    }

    fun setTitleText(text: String) {
        tfTitle?.setTextAndCheckShow(text)
    }

    fun setListener(listener: AddOnComponentListener) {
        this.listener = listener
    }

    fun getAddonData(productId: String, warehouseId: String, isTokocabang: Boolean) {
        viewModel.getAddOn(productId, warehouseId, isTokocabang)
    }

    fun setSelectedAddons(selectedAddonIds: List<String>) {
        viewModel.setSelectedAddOn(selectedAddonIds)
    }
}
