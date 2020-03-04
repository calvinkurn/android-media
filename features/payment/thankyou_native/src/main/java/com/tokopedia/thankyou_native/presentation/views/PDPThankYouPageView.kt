package com.tokopedia.thankyou_native.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.thankyou_native.R
import javax.inject.Inject

class PDPThankYouPageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    fun getLayout() = R.layout.thank_pdp_recommendation

    init {
        initUI()
    }

    private fun initUI() {
        val v = LayoutInflater.from(context).inflate(getLayout(), this, true)
        setupRecyclerView(v)
    }

    private fun injectComponents() {
        /*val component = DaggerPdpComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
        component.inject(this)


        if (context is AppCompatActivity) {
            val viewModelProvider = ViewModelProviders.of(context as AppCompatActivity, viewModelFactory)
            viewModel = viewModelProvider[PdpDialogViewModel::class.java]
        }*/
    }

    private fun setupRecyclerView(view: View) {

    }


    private fun getRecommendationListener(): RecommendationListener {
        return object : RecommendationListener {
            override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onProductImpression(item: RecommendationItem) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
    }


}