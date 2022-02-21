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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.applink.di.component.AppLinkComponent
import com.tokopedia.developer_options.applink.presentation.adapter.AppLinkItemAdapter
import com.tokopedia.developer_options.applink.presentation.uimodel.AppLinkUiModel
import com.tokopedia.developer_options.applink.presentation.viewmodel.AppLinkViewModel
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

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(AppLinkViewModel::class.java)
    }

    private val appLinkItemAdapter by lazy { AppLinkItemAdapter(this) }

    private var rvAppLinkList: RecyclerView? = null
    private var searchbarAppLink: SearchBarUnify? = null
    private var swipeRefreshAppLink: SwipeRefreshLayout? = null
    private var tvAppLinkNotFound: Typography? = null
    private var btnAppLinkRoute: UnifyButton? = null

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
        searchbarAppLink?.searchBarTextField?.setText(appLink)
    }

    private fun routeToAppLink() {
        val appLink = searchbarAppLink?.searchBarTextField?.text?.toString().orEmpty()
        btnAppLinkRoute?.setOnClickListener {
            if (!RouteManager.route(context, appLink)) {
                Toaster.build(
                    it,
                    "Applink is not supported, please check the value from searchbar"
                ).show()
            }
        }
    }

    private fun initSearchbarView() {
        searchbarAppLink?.searchBarTextField?.apply {
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
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    appLinkItemAdapter.filter.filter(s?.toString().orEmpty())
                }
            })
        }
    }

    private fun initViews(view: View) {
        with(view) {
            rvAppLinkList = findViewById(R.id.rvAppLinkList)
            searchbarAppLink = findViewById(R.id.searchbarAppLink)
            swipeRefreshAppLink = findViewById(R.id.swipeRefreshAppLink)
            tvAppLinkNotFound = findViewById(R.id.tvAppLinkNotFound)
            btnAppLinkRoute = findViewById(R.id.btnAppLinkRoute)
        }
    }


    private fun loadData() {
        viewModel.getAppLinkItemList()
    }

    private fun setupRvAdapter() {
        rvAppLinkList?.run {
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
                    tvAppLinkNotFound?.show()
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
            tvAppLinkNotFound?.hide()
        } else {
            tvAppLinkNotFound?.show()
        }
    }

    companion object {
        @JvmStatic
        val TAG: String = AppLinkListFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): AppLinkListFragment {
            return AppLinkListFragment()
        }
    }
}