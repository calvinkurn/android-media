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
import com.tokopedia.addon.presentation.viewmodel.AddOnViewModel
import com.tokopedia.product_service_widget.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class AddOnWidgetView : BaseCustomView {

    @Inject
    lateinit var viewModel: AddOnViewModel
    private var addonAdapter: AddOnAdapter = AddOnAdapter()
    private var tfTitle: Typography? = null

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

            }
            viewModel.getAddOnResult
            viewModel.getAddOn("2148784281")
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
            addonAdapter?.setItems(listOf(
                Pair(
                    "Title Satu",
                    listOf(
                        "Name satu",
                        "Name dua",
                    )
                ),
                Pair(
                    "Title Dua",
                    listOf(
                        "Name satu",
                        "Name dua",
                    )
                )
            ))
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

    fun setTitleText(text: String) {
        tfTitle?.text = text
    }
}
