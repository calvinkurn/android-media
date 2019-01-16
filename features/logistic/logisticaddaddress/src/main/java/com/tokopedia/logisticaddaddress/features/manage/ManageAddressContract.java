package com.tokopedia.logisticaddaddress.features.manage;

import android.content.DialogInterface;

import com.tokopedia.logisticaddaddress.adapter.AddressViewModel;
import com.tokopedia.logisticdata.data.entity.address.AddressModel;
import com.tokopedia.logisticdata.data.entity.address.Token;

import java.util.List;

/**
 * Created by Fajar Ulin Nuha on 13/11/18.
 */
public interface ManageAddressContract {

    interface View {

        MPAddressActivityListener getActivityListener();

        void refreshView();

        void filter(int sortId, String query);

        void openFormAddressView(AddressModel data);

        void showData(List<AddressViewModel> data, boolean hasNext);

        void showLoadingView();

        void showDialogConfirmation(String message, DialogInterface.OnClickListener onPositiveClickListener);

        void toggleFilterFab(boolean isVisible);

        void showErrorSnackbar(String message);

        void showNetworkError();

        void setIsEmptyAddress(boolean isEmpty);

    }

    interface Presenter {

        void setView(ManageAddressContract.View view);

        void detachView();

        void getAddress(int page, int sortId, String query);

        void deleteAddress(String id);

        void prioritizeAddress(String id);

        Token getToken();

    }
}
