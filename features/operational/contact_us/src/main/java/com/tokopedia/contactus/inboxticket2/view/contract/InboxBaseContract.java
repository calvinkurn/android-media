package com.tokopedia.contactus.inboxticket2.view.contract;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by pranaymohapatra on 18/06/18.
 */

public interface InboxBaseContract {
    interface InboxBaseView extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void showProgressBar();

        void hideProgressBar();

        android.view.View getRootView();

        int getRequestCode();

        Fragment getFragment();

        FragmentManager getFragmentManagerInstance();

        void showBottomFragment();

        void hideBottomFragment();

        void updateDataSet();

    }

    interface InboxBasePresenter extends CustomerPresenter<InboxBaseView> {
        void onActivityResult(int requestCode,int resultCode,Intent data);

        void onDestroy();

        String getSCREEN_NAME();

        BottomSheetDialogFragment getBottomFragment();

        boolean onOptionsItemSelected(MenuItem item);
    }
}
