package com.tokopedia.sellerapp.gmsubscribe.view;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.sellerapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sebastianuskh on 11/14/16.
 */

public class GMSubscribeBlockFragment extends Fragment {

    @OnClick(R.id.button_block_gm_subscribe)
    void goToBrowser (){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLGenerator.generateURLSessionLogin("http://gold.tokopedia.com", getActivity())));
        startActivity(browserIntent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gm_subscribe_block, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
