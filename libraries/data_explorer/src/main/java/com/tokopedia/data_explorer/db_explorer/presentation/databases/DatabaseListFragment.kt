package com.tokopedia.data_explorer.db_explorer.presentation.databases

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.db_explorer.di.DataExplorerComponent
import com.tokopedia.data_explorer.db_explorer.domain.databases.models.DatabaseDescriptor
import com.tokopedia.data_explorer.db_explorer.domain.databases.models.DatabaseInteractions
import com.tokopedia.data_explorer.db_explorer.presentation.Constants
import com.tokopedia.data_explorer.db_explorer.presentation.Searchable
import com.tokopedia.data_explorer.db_explorer.presentation.schema.SchemaActivity
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_database_list_layout.*
import javax.inject.Inject

class DatabaseListFragment : BaseDaggerFragment(), Searchable {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val viewModel: DatabaseViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(DatabaseViewModel::class.java)
    }

    private val databaseAdapter: DatabaseAdapter by lazy(LazyThreadSafetyMode.NONE) {
        DatabaseAdapter(
            onClick = { navigateToTableActivity(it) },
            interaction = DatabaseInteractions(
                onDelete = { showDeleteDialog(it) },
                onCopy = { viewModel.copyDatabase(it) },
                onShare = { showToast("Oops! Feature still in progress", Toaster.TYPE_NORMAL)}
            )
        )
    }

    private fun showDeleteDialog(databaseDescriptor: DatabaseDescriptor) {
        context?.let {
            DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle("Delete Database")
                setDescription("Are you sure you want to delete ${databaseDescriptor.name}")
                setPrimaryCTAText("Yes Delete !")
                setSecondaryCTAText("No!")
                setPrimaryCTAClickListener {
                    viewModel.removeDatabase(databaseDescriptor)
                    dismiss()
                }
                setSecondaryCTAClickListener {
                    dismiss()
                }
                show()
            }
        }
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
        setUpSearch()
        rvDatabaseList.adapter = databaseAdapter
        rvDatabaseList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setUpSearch() {
        searchInputView.searchBarTextField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.apply { search(this) } ?: run { search(null)}
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        searchInputView.clearListener = { search(null) }
    }

    private fun observeViewModels() {
        viewModel.databaseListLiveData.observe(viewLifecycleOwner, { list ->
            databaseAdapter.submitList(list)
        })
        viewModel.errorLiveData.observe(viewLifecycleOwner, {
            Toaster.build(
                rvDatabaseList,
                it.message ?: "",
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_ERROR
            ).show()
        })
        viewModel.actionPerformedLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> showToast(it.data, Toaster.TYPE_NORMAL)
                is Fail -> showToast(it.throwable.localizedMessage, Toaster.TYPE_NORMAL)
            }
        })
    }

    private fun showToast(msg: String, type: Int) {
        Toaster.build(rvDatabaseList, msg, Toaster.LENGTH_SHORT, type).show()
    }

    override fun getScreenName() = ""
    override fun initInjector() = getComponent(DataExplorerComponent::class.java).inject(this)

    companion object {
        fun newInstance() = DatabaseListFragment()
    }

    override fun search(query: String?) {
        viewModel.browseDatabases(query)
    }

    override fun searchQuery() = searchInputView.searchBarTextField.text.toString()

}