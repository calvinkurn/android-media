package com.rahullohra.fakeresponse.presentation.fragments

import androidx.lifecycle.Observer
import com.rahullohra.fakeresponse.R
import com.rahullohra.fakeresponse.data.diProvider.fragments.DownloadFragmentProvider
import com.rahullohra.fakeresponse.presentation.activities.FakeResponseActivity
import com.rahullohra.fakeresponse.presentation.livedata.Fail
import com.rahullohra.fakeresponse.presentation.livedata.Loading
import com.rahullohra.fakeresponse.presentation.livedata.Success
import com.rahullohra.fakeresponse.presentation.viewmodels.DownloadFragmentVM
import kotlinx.android.synthetic.main.gql_fragment_download.*

class DownloadFragment : BaseFragment() {

    lateinit var viewModel: DownloadFragmentVM

    companion object {
        fun newInstance(): DownloadFragment {
            return DownloadFragment()
        }
    }

    override fun getLayout() = R.layout.gql_fragment_download

    override fun setupFragment() {
        injectComponents()
        setListeners()
        getData()
    }

    fun setListeners() {
        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Loading -> {
                    tvStatus.text = "Downloading.."
                    btnRetry.isEnabled = false
                }
                is Success -> {
                    tvStatus.text = "Download Success"
                    btnRetry.isEnabled = false
                    handleOnDownloadSuccess()
                }
                is Fail -> {
                    btnRetry.isEnabled = true
                    tvStatus.text = "Download Fail: ${it.ex.message}"
                }
            }
        })
    }

    fun handleOnDownloadSuccess() {
        if (context is FakeResponseActivity) {
            (context as FakeResponseActivity).onSqlFilesArePresent()
        }
    }

    fun getData() {
        viewModel.downloadSqliteFiles()
    }

    fun injectComponents() {
        DownloadFragmentProvider().inject(this)
    }


}
