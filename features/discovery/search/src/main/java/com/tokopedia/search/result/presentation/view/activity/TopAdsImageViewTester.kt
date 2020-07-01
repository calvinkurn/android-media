package com.tokopedia.search.result.presentation.view.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.search.R
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageVieWApiResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.widget.TopAdsImageView

class TopAdsImageViewTester : AppCompatActivity() {

    private lateinit var adsBannerView: TopAdsImageView
    private lateinit var adsBannerView2: TopAdsImageView
    private lateinit var button: Button
    private lateinit var id: EditText
    private lateinit var count: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_ads_image_view_tester)

        adsBannerView = findViewById(R.id.ads_banner)
        adsBannerView2 = findViewById(R.id.ads_banner2)
        button = findViewById(R.id.loadImage)
        id = findViewById(R.id.imageId)
        count = findViewById(R.id.adsCount)

        button.setOnClickListener {
            adsBannerView.getImageData("iphone","2",count.text.toString(),id.text.toString().toInt())
        }




        adsBannerView.setApiResponseListener(object : TopAdsImageVieWApiResponseListener {

            override fun onImageViewResponse(imageDataList: ArrayList<TopAdsImageViewModel>) {
                if (imageDataList.size==1){
                    adsBannerView.loadImage(imageDataList[0])
                }  else if(imageDataList.size==2){
                    adsBannerView.loadImage(imageDataList[0])
                    adsBannerView2.loadImage(imageDataList[1])
                }


            }

            override fun onError(t: Throwable) {
                //handle on error
            }
        })

        adsBannerView.setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
            override fun onTopAdsImageViewClicked(applink: String?) {
                Toast.makeText(this@TopAdsImageViewTester, applink, Toast.LENGTH_SHORT).show()
            }

        })
        adsBannerView2.setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
            override fun onTopAdsImageViewClicked(applink: String?) {
                Toast.makeText(this@TopAdsImageViewTester, applink, Toast.LENGTH_SHORT).show()
            }

        })

        adsBannerView.setTopAdsImageViewImpression(object :TopAdsImageViewImpressionListener{
            override fun onTopAdsImageViewImpression(viewUrl: String) {
                Toast.makeText(this@TopAdsImageViewTester, viewUrl, Toast.LENGTH_SHORT).show()

            }
        })

        adsBannerView2.setTopAdsImageViewImpression(object :TopAdsImageViewImpressionListener{
            override fun onTopAdsImageViewImpression(viewUrl: String) {
                Toast.makeText(this@TopAdsImageViewTester, viewUrl, Toast.LENGTH_SHORT).show()

            }
        })

    }
}