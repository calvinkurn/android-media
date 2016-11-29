package com.tokopedia.sellerapp.gmsubscribe.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.gmsubscribe.presenter.GMSubscribePresenter;
import com.tokopedia.sellerapp.gmsubscribe.presenter.GMSubscribePresenterImpl;
import com.tokopedia.sellerapp.home.view.SellerHomeActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by sebastianuskh on 11/8/16.
 */

public class GMSubscribeFragment extends Fragment implements
        GMSubscribeWebView.GMSubscribeWebViewListener,
        GMSubscribeView {

    private static final String TAG = "GMSubscribeFragment";

    @Bind(R.id.webview)
    GMSubscribeWebView webView;

    @Bind(R.id.progressbar)
    ProgressBar progressBar;

    private GMSubscribePresenter presenter;
    private CompositeSubscription compositeSubscribtion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init presenter
        presenter = new GMSubscribePresenterImpl(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // init view and webview
        Log.d(TAG, "onCreateView, creating webview");
        View view = inflater.inflate(R.layout.fragment_gm_subscribe, container, false);
        ButterKnife.bind(this, view);
        webView.initGMSubscribeWebView(this, progressBar);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(compositeSubscribtion == null || compositeSubscribtion.isUnsubscribed()){
            compositeSubscribtion = new CompositeSubscription();
        }
        Log.d(TAG, "onresume, go to web site gold.tokopedia.com");
        webView.goToGoldTokopedia(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        if(compositeSubscribtion != null){
            compositeSubscribtion.unsubscribe();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @Override
    public void goToHome() {
        getActivity().startActivity(new Intent(getActivity(), SellerHomeActivity.class));
    }

    @Override
    public void thankYouPageCount() {
        if (getActivity() instanceof GMSubscribeFragmentListener) {
            ((GMSubscribeFragmentListener) getActivity()).thankYouPageCount();
        }
    }

    public interface GMSubscribeFragmentListener {
        void thankYouPageCount();
    }
}

