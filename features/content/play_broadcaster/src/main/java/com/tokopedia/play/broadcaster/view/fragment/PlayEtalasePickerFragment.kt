package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.di.DaggerPlayBroadcasterComponent
import com.tokopedia.play.broadcaster.view.adapter.PlayEtalaseAdapter
import com.tokopedia.play.broadcaster.view.custom.PlaySearchBar
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
import javax.inject.Inject

/**
 * Created by jegul on 26/05/20
 */
class PlayEtalasePickerFragment : PlayBaseSetupFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: PlayEtalasePickerViewModel

    private lateinit var tvInfo: TextView
    private lateinit var psbSearch: PlaySearchBar
    private lateinit var rvEtalase: RecyclerView

    private val etalaseAdapter = PlayEtalaseAdapter()

    override fun getTitle(): String = "Select Products or Collection"

    override fun isRootFragment(): Boolean = true

    override fun getScreenName(): String = "Play Etalase Picker"

    override fun initInjector() {
        DaggerPlayBroadcasterComponent.create()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayEtalasePickerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_etalase_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeEtalase()
    }

    private fun initView(view: View) {
        with(view) {
            tvInfo = findViewById(R.id.tv_info)
            psbSearch = findViewById(R.id.psb_search)
            rvEtalase = findViewById(R.id.rv_etalase)
        }
    }

    private fun setupView(view: View) {
        psbSearch.isEnabled = false
        psbSearch.setOnClickListener {
            enterSearchMode()
        }

        rvEtalase.apply {
            adapter = etalaseAdapter
        }
    }

    private fun enterSearchMode() {

    }

    private fun exitSearchMode() {

    }

    //region observe
    /**
     * Observe
     */
    private fun observeEtalase() {
        viewModel.observableEtalase.observe(viewLifecycleOwner, Observer {
            etalaseAdapter.setItemsAndAnimateChanges(it)
        })
    }
    //endregion

    companion object {

        fun newInstance(): PlayEtalasePickerFragment = PlayEtalasePickerFragment()
    }
}