package com.tokopedia.developer_options.applink.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.applink.di.component.AppLinkComponent
import com.tokopedia.developer_options.applink.presentation.adapter.AppLinkItemAdapter
import com.tokopedia.developer_options.applink.presentation.uimodel.AppLinkUiModel
import com.tokopedia.developer_options.applink.presentation.viewmodel.AppLinkViewModel
import com.tokopedia.developer_options.applink.utils.DeepLinkFileUtils
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AppLinkListFragment : BaseDaggerFragment(), AppLinkItemAdapter.AppLinkItemListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var deepLinkFileUtils: DeepLinkFileUtils

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(AppLinkViewModel::class.java)
    }

    private val appLinkItemAdapter by lazy { AppLinkItemAdapter(this) }

    private var appLinkListRv: RecyclerView? = null
    private var appLinkSearchbar: SearchBarUnify? = null
    private var appLinkNotFoundTv: Typography? = null
    private var appLinkRouteBtn: UnifyButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_applink_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupRvAdapter()
        initSearchbarView()
        routeToAppLink()
        observeAppLinkList()
        loadData()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(AppLinkComponent::class.java).inject(this)
    }

    override fun onAppLinkItemClicked(appLink: String) {
        appLinkSearchbar?.searchBarTextField?.run {
            setText(appLink)
            setSelection(text.length)
            requestFocus()
        }
    }

    private fun routeToAppLink() {
        appLinkRouteBtn?.setOnClickListener { v ->
            if (RouteManager.isSupportApplink(context, getAppLinkFromSearchbar())) {
                RouteManager.route(context, getAppLinkFromSearchbar())
            } else {
                Toaster.build(
                    v,
                    getString(R.string.message_applink_failed)
                ).show()
            }
        }
    }

    private fun getAppLinkFromSearchbar() =
        appLinkSearchbar?.searchBarTextField?.text?.toString().orEmpty()

    private fun initSearchbarView() {
        appLinkSearchbar?.searchBarTextField?.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = text.toString()
                    appLinkItemAdapter.filter.filter(query)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    appLinkItemAdapter.filter.filter(s?.toString().orEmpty())
                }
            })
        }
    }

    private fun initViews(view: View) {
        with(view) {
            appLinkListRv = findViewById(R.id.appLinkListRv)
            appLinkSearchbar = findViewById(R.id.appLinkSearchbar)
            appLinkNotFoundTv = findViewById(R.id.appLinkNotFoundTv)
            appLinkRouteBtn = findViewById(R.id.appLinkRouteBtn)
        }
    }


    private fun loadData() {
        viewModel.getAppLinkItemList(
            deepLinkFileUtils.getJsonFromRaw(
                resources, DEEPLINK_RESOURCE
            )
        )
    }

    private fun setupRvAdapter() {
        appLinkListRv?.run {
            layoutManager = context?.let { LinearLayoutManager(it) }
            adapter = appLinkItemAdapter
        }
    }

    private fun observeAppLinkList() {
        observe(viewModel.appLinkItemList) {
            appLinkItemAdapter.clearAppLinkList()
            when (it) {
                is Success -> {
                    setAppLinkList(it.data)
                }
                is Fail -> {
                    appLinkNotFoundTv?.show()
                    view?.let { view ->
                        Toaster.build(
                            view,
                            it.throwable.localizedMessage.orEmpty(),
                            Toaster.LENGTH_SHORT,
                            Toaster.TYPE_ERROR
                        ).show()
                    }
                }
            }
        }
    }

    private fun setAppLinkList(appLinkList: List<AppLinkUiModel>) {
        if (appLinkList.isNotEmpty()) {
            appLinkItemAdapter.setAppLinkList(appLinkList)
            appLinkNotFoundTv?.hide()
        } else {
            appLinkNotFoundTv?.show()
        }
    }

    companion object {
        @JvmStatic
        val TAG: String = AppLinkListFragment::class.java.simpleName

        private val DEEPLINK_RESOURCE = R.raw.deeplink

        @JvmStatic
        fun newInstance(): AppLinkListFragment {
            return AppLinkListFragment()
        }
    }
}