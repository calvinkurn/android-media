package com.tokopedia.posapp.product.productdetail.view.activity;

import android.text.util.Linkify;
import android.view.View;

import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpdpdp.DescriptionActivity;

/**
 * @author okasurya on 5/7/18.
 */

public class PosDescriptionActivity extends DescriptionActivity {
    @Override
    public void showData() {
        String productDescription = getIntent().getStringExtra(KEY_DESCRIPTION);
        tvDesc.setText(productDescription == null
                || productDescription.equals("")
                || productDescription.equals("0")
                ? getResources().getString(com.tokopedia.tkpdpdp.R.string.no_description_pdp) : MethodChecker.fromHtml(productDescription));
        tvDesc.setAutoLinkMask(0);
        Linkify.addLinks(tvDesc, Linkify.WEB_URLS);
        descriptionContainer.setVisibility(View.VISIBLE);
        VideoData videoData = getIntent().getParcelableExtra(KEY_VIDEO);
        if (videoData != null) {
            productVideoHorizontalScroll.setVisibility(View.VISIBLE);
            productVideoHorizontalScroll.renderData(videoData,this);
        } else {
            productVideoHorizontalScroll.setVisibility(View.GONE);
        }
    }
}
