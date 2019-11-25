package com.tokopedia.gamification.pdp.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ViewFlipper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.gamification.R
import com.tokopedia.gamification.pdp.viewmodels.PdpDialogVIewModel
import com.tokopedia.unifyprinciples.Typography

class PdpDialog {

    private val CONTAINER_LOADING = 0
    private val CONTAINER_LIST = 1
    private val CONTAINER_ERROR = 2

    private lateinit var tvTitle: Typography
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewFlipper: ViewFlipper

    lateinit var viewModel:PdpDialogVIewModel

    fun getLayout() = R.layout.dialog_pdp_gamification

    fun showDialog(context: Context) {
        val bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(context)
        val v = LayoutInflater.from(context).inflate(getLayout(), null)
        bottomSheet.setContentView(v)
        bottomSheet.show()
        initViews(v)
        return
    }

    private fun initViews(root: View) {
        recyclerView = root.findViewById(com.tokopedia.promotionstarget.R.id.recyclerView)
        viewFlipper = root.findViewById(com.tokopedia.promotionstarget.R.id.viewFlipper)
        tvTitle = root.findViewById(com.tokopedia.promotionstarget.R.id.tvTitle)

        injectComponents()
    }

    private fun injectComponents() {

    }



}