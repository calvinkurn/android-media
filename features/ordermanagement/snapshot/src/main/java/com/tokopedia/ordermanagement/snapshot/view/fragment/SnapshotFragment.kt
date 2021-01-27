package com.tokopedia.ordermanagement.snapshot.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.design.utils.StringUtils
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotTypeData
import com.tokopedia.ordermanagement.snapshot.di.SnapshotComponent
import com.tokopedia.ordermanagement.snapshot.util.SnapshotConsts
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotAdapter
import com.tokopedia.ordermanagement.snapshot.view.viewmodel.SnapshotViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class SnapshotFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var snapshotAdapter: SnapshotAdapter
    private var rv: RecyclerView? = null

    private val snapshotViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SnapshotViewModel::class.java]
    }

    companion object {
        fun newInstance() = SnapshotFragment()
    }

    private lateinit var viewModel: SnapshotViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val contentView = inflater.inflate(R.layout.snapshot_fragment, container, false)
        rv = contentView.findViewById(R.id.rv_snapshot)
        return contentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SnapshotViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            DaggerSnapshotComponent.builder()
                    .baseAppComponent(getBaseAppComponent())
                    .snapshotModule(context?.let { SnapshotComponent(it) })
                    .build()
                    .inject(this)
        }
    }

    private fun getBaseAppComponent(): BaseAppComponent {
        return (activity?.application as BaseMainApplication).baseAppComponent
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareLayout()
        renderPage()
    }

    private fun prepareLayout() {
        snapshotAdapter = SnapshotAdapter()
        rv?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = snapshotAdapter
        }
    }

    private fun renderPage() {
        val listPage = arrayListOf<SnapshotTypeData>()
        listPage.add(SnapshotTypeData(2, SnapshotConsts.TYPE_HEADER))
        listPage.add(SnapshotTypeData("a", SnapshotConsts.TYPE_INFO))
        listPage.add(SnapshotTypeData("b", SnapshotConsts.TYPE_SHOP))
        listPage.add(SnapshotTypeData("c", SnapshotConsts.TYPE_DETAILS))
        snapshotAdapter.addList(listPage)
    }

    private fun observingSnapshot() {
        snapshotViewModel.snapshotResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    // lanjut dsini untuk rendering page nya
                    val listPage = arrayListOf<SnapshotTypeData>()
                    listPage.add(SnapshotTypeData(it.data.productImageSecondary, SnapshotConsts.TYPE_HEADER))

                    val msg = StringUtils.convertListToStringDelimiter(it.data.atcMulti.buyAgainData.message, ",")
                    if (it.data.atcMulti.buyAgainData.success == 1) {
                        showToasterAtc(msg, Toaster.TYPE_NORMAL)
                    } else {
                        showToaster(msg, Toaster.TYPE_ERROR)
                    }
                }
                is Fail -> {
                    context?.getString(R.string.fail_cancellation)?.let { it1 -> showToaster(it1, Toaster.TYPE_ERROR) }
                }
            }
        })
    }
}