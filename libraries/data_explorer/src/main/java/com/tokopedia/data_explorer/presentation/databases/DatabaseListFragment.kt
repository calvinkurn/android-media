package com.tokopedia.data_explorer.presentation.databases

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.di.DbInspectorComponent
import com.tokopedia.data_explorer.domain.databases.models.DatabaseDescriptor
import com.tokopedia.data_explorer.presentation.Constants
import com.tokopedia.data_explorer.presentation.schema.SchemaActivity
import kotlinx.android.synthetic.main.fragment_database_list_layout.*
import javax.inject.Inject

class DatabaseListFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val viewModel: DatabaseViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(DatabaseViewModel::class.java)
    }

    private val databaseAdapter: DatabaseAdapter by lazy(LazyThreadSafetyMode.NONE) {
        DatabaseAdapter(
            onClick = { navigateToTableActivity(it) }
        )
    }

    private fun navigateToTableActivity(databaseDescriptor: DatabaseDescriptor) {
        context?.startActivity(
            Intent(requireContext(), SchemaActivity::class.java).apply {
                putExtra(Constants.Keys.DATABASE_NAME, databaseDescriptor.name)
                putExtra(Constants.Keys.DATABASE_PATH, databaseDescriptor.absolutePath)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_database_list_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.browseDatabases()
        observeViewModels()
        rvDatabaseList.adapter = databaseAdapter
        rvDatabaseList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun observeViewModels() {
        viewModel.databaseListLiveData.observe(viewLifecycleOwner, { list ->
            databaseAdapter.submitList(list)
        })
        viewModel.errorLiveData.observe(viewLifecycleOwner, {
            databaseAdapter.submitList(arrayListOf())
        })
    }


    override fun getScreenName() = ""
    override fun initInjector() = getComponent(DbInspectorComponent::class.java).inject(this)

    companion object {
        fun newInstance() = DatabaseListFragment()
    }
}