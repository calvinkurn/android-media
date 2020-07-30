package com.tokopedia.events.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import androidx.fragment.app.FragmentManager;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface EventBaseContract {

    interface EventBaseView extends CustomerView {
        Activity getActivity();

        Resources getViewResources();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void showProgressBar();

        void hideProgressBar();

        void showSnackBar(String message, boolean clickable);

        void showToast(String message, int duration);

        android.view.View getRootView();

        FragmentManager getSupportFragmentManager();
    }

    interface EventBasePresenter extends CustomerPresenter<EventBaseView> {
        boolean onClickOptionMenu(int id);

        void onBackPressed();

        void onActivityResult(int requestCode,int resultCode);

        void onDestroy();
    }
}
