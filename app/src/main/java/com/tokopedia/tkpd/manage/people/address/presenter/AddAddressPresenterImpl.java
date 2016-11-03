package com.tokopedia.tkpd.manage.people.address.presenter;

import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.tkpd.library.utils.data.DataManagerImpl;
import com.tkpd.library.utils.data.DataReceiver;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.addtocart.model.responseatcform.Destination;
import com.tokopedia.tkpd.database.model.Bank;
import com.tokopedia.tkpd.database.model.CategoryDB;
import com.tokopedia.tkpd.database.model.City;
import com.tokopedia.tkpd.database.model.District;
import com.tokopedia.tkpd.database.model.Province;
import com.tokopedia.tkpd.manage.people.address.ManageAddressConstant;
import com.tokopedia.tkpd.manage.people.address.fragment.adapter.ProvinceAdapter;
import com.tokopedia.tkpd.manage.people.address.fragment.adapter.RegencyAdapter;
import com.tokopedia.tkpd.manage.people.address.fragment.adapter.SubDistrictAdapter;
import com.tokopedia.tkpd.manage.people.address.interactor.AddAddressRetrofitInteractor;
import com.tokopedia.tkpd.manage.people.address.interactor.AddAddressRetrofitInteractorImpl;
import com.tokopedia.tkpd.manage.people.address.listener.AddAddressFragmentView;

import java.util.List;
import java.util.Map;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by nisie on 9/6/16.
 */
public class AddAddressPresenterImpl implements AddAddressPresenter, ManageAddressConstant {


    private final AddAddressFragmentView viewListener;
    private final AddAddressRetrofitInteractor networkInteractor;
    private final CompositeSubscription compositeSubscription;
    private Destination address;

    public AddAddressPresenterImpl(AddAddressFragmentView viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new AddAddressRetrofitInteractorImpl();
        this.compositeSubscription = new CompositeSubscription();
        this.address = new Destination();

    }

    @Override
    public void saveAddress() {
        viewListener.removeError();
        if (isValidAddress()) {
            sendAddress();
        }
    }

    private void sendAddress() {
        viewListener.showLoading();
        if (viewListener.getArguments().getBoolean(IS_EDIT, false)) {
            networkInteractor.editAddress(viewListener.getActivity(), getParam(), new AddAddressRetrofitInteractor.AddAddressListener() {
                @Override
                public void onSuccess(String address_id) {
                    viewListener.finishLoading();
                    viewListener.finishActivity(address);
                }

                @Override
                public void onTimeout(String message) {
                    viewListener.finishLoading();
                    viewListener.showErrorSnackbar("");
                }

                @Override
                public void onError(String error) {
                    viewListener.finishLoading();
                    viewListener.showErrorSnackbar(error);
                }

                @Override
                public void onNullData() {
                    viewListener.finishLoading();
                    viewListener.showErrorSnackbar("");
                }

                @Override
                public void onNoNetworkConnection() {
                    viewListener.finishLoading();
                    viewListener.showErrorSnackbar("");
                }
            });
        } else {
            networkInteractor.addAddress(viewListener.getActivity(), getParam(), new AddAddressRetrofitInteractor.AddAddressListener() {
                @Override
                public void onSuccess(String address_id) {
                    viewListener.finishLoading();
                    if(!address_id.equals(""))
                        address.setAddressId(address_id);
                    viewListener.finishActivity(address);
                }

                @Override
                public void onTimeout(String message) {
                    viewListener.finishLoading();
                    viewListener.showErrorSnackbar("");
                }

                @Override
                public void onError(String error) {
                    viewListener.finishLoading();
                    viewListener.showErrorSnackbar(error);
                }

                @Override
                public void onNullData() {
                    viewListener.finishLoading();
                    viewListener.showErrorSnackbar("");
                }

                @Override
                public void onNoNetworkConnection() {
                    viewListener.finishLoading();
                    viewListener.showErrorSnackbar("");
                }
            });
        }
    }

    private Map<String, String> getParam() {
        address.setAddressName(viewListener.getAddressType().getText().toString());
        address.setAddressStreet(viewListener.getAddress().getText().toString());
        address.setCityId(((RegencyAdapter) viewListener.getSpinnerRegency().getAdapter()).getList().get(viewListener.getSpinnerRegency().getSelectedItemPosition() - 1).getCityId());
        address.setCityName(((RegencyAdapter) viewListener.getSpinnerRegency().getAdapter()).getList().get(viewListener.getSpinnerRegency().getSelectedItemPosition() - 1).getCityName());
        address.setDistrictId(((SubDistrictAdapter) viewListener.getSpinnerSubDistrict().getAdapter()).getList().get(viewListener.getSpinnerSubDistrict().getSelectedItemPosition() - 1).getDistrictId());
        address.setDistrictName(((SubDistrictAdapter) viewListener.getSpinnerSubDistrict().getAdapter()).getList().get(viewListener.getSpinnerSubDistrict().getSelectedItemPosition() - 1).getDistrictName());
        address.setProvinceId(((ProvinceAdapter) viewListener.getSpinnerProvince().getAdapter()).getList().get(viewListener.getSpinnerProvince().getSelectedItemPosition() - 1).getProvinceId());
        address.setProvinceName(((ProvinceAdapter) viewListener.getSpinnerProvince().getAdapter()).getList().get(viewListener.getSpinnerProvince().getSelectedItemPosition() - 1).getProvinceName());
        address.setReceiverName(viewListener.getReceiverName().getText().toString());
        address.setReceiverPhone(viewListener.getReceiverPhone().getText().toString());
        address.setPostalCode(viewListener.getPostCode().getText().toString());
        if (viewListener.getArguments().getBoolean(IS_EDIT, false)) {
            Destination editParam = viewListener.getArguments().getParcelable(EDIT_PARAM);
            if (editParam != null)
                address.setAddressId(editParam.getAddressId());
            address.setPassword(viewListener.getPassword().getText().toString());
            return address.getParamEditAddress();
        } else {
            return address.getParamAddAddress();
        }
    }

    @Override
    public void getListProvince() {
        viewListener.setActionsEnabled(false);
        viewListener.showLoading();
        DataManagerImpl.getDataManager().getListProvince(viewListener.getActivity(), getDataReceiver(GET_LIST_PROVINCE));
    }

    @Override
    public void onProvinceSelected(int pos) {
        viewListener.resetRegency();
        viewListener.hideSubDistrict();
        viewListener.resetSubDistrict();
        if (pos != 0) {
            getListCity(viewListener.getProvinceAdapter().getList().get(pos - 1));
        }
    }

    @Override
    public void onRegencySelected(int pos) {
        viewListener.resetSubDistrict();
        if (pos != 0) {
            getListDistrict(viewListener.getRegencyAdapter().getList().get(pos - 1));
        }
    }

    @Override
    public LatLng getLatLng() {
        return address != null ? address.getLatLng() : null;
    }

    @Override
    public void setLatLng(String latitude, String longitude) {
        LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        address.setLatLng(latLng);
    }

    @Override
    public void getListDistrict(City city) {
        viewListener.showLoadingDistrict();
        viewListener.setActionsEnabled(false);
        DataManagerImpl.getDataManager().getListDistrict(viewListener.getActivity(), getDataReceiver(GET_LIST_DISTRICT), city.getCityId());
    }


    @Override
    public void getListCity(Province province) {
        viewListener.showLoadingRegency();
        viewListener.setActionsEnabled(false);
        DataManagerImpl.getDataManager().getListCity(viewListener.getActivity(), getDataReceiver(GET_LIST_CITY), province.getProvinceId());
    }

    private boolean isValidAddress() {
        boolean isValid = true;

        if (viewListener.getArguments().getBoolean(IS_EDIT, false) &&
                viewListener.getPassword().getText().length() == 0) {
            viewListener.setError(viewListener.getPasswordLayout(), viewListener.getString(R.string.error_field_required));
            viewListener.getPassword().requestFocus();
            isValid = false;
        }

        if (viewListener.getReceiverPhone().getText().length() == 0) {
            viewListener.setError(viewListener.getReceiverPhoneLayout(), viewListener.getString(R.string.error_field_required));
            viewListener.getReceiverPhone().requestFocus();
            isValid = false;
        } else if (viewListener.getReceiverPhone().getText().length() < 6) {
            viewListener.setError(viewListener.getReceiverPhoneLayout(), viewListener.getString(R.string.error_min_phone_numer));
            viewListener.getReceiverPhone().requestFocus();
            isValid = false;
        } else if (viewListener.getReceiverPhone().getText().length() > 20) {
            viewListener.setError(viewListener.getReceiverPhoneLayout(), viewListener.getString(R.string.error_max_phone_numer));
            viewListener.getReceiverPhone().requestFocus();
            isValid = false;
        }
        if (viewListener.getPostCode().getText().length() == 0) {
            viewListener.setError(viewListener.getPostCodeLayout(), viewListener.getString(R.string.error_field_required));
            viewListener.getPostCode().requestFocus();
            isValid = false;
        } else if (viewListener.getPostCode().getText().length() < 5) {
            viewListener.setError(viewListener.getPostCodeLayout(), viewListener.getString(R.string.error_min_5_character));
            viewListener.getPostCode().requestFocus();
            isValid = false;
        } else if (viewListener.getPostCode().getText().length() > 10) {
            viewListener.setError(viewListener.getPostCodeLayout(), viewListener.getString(R.string.error_max_post_code));
            viewListener.getPostCode().requestFocus();
            isValid = false;
        }
        if (viewListener.getAddress().getText().length() <= 20) {
            viewListener.setError(viewListener.getAddressLayout(), viewListener.getString(R.string.error_min_address));
            viewListener.getAddress().requestFocus();
            isValid = false;
        }
        if (viewListener.getAddress().getText().length() == 0) {
            viewListener.setError(viewListener.getAddressLayout(), viewListener.getString(R.string.error_field_required));
            viewListener.getAddress().requestFocus();
            isValid = false;
        }
        if (viewListener.getReceiverName().getText().length() == 0) {
            viewListener.setError(viewListener.getReceiverNameLayout(), viewListener.getString(R.string.error_field_required));
            viewListener.getReceiverName().requestFocus();
            isValid = false;
        }
        if (viewListener.getReceiverName().getText().length() > 128) {
            viewListener.setError(viewListener.getReceiverNameLayout(), viewListener.getString(R.string.error_max_128_character));
            viewListener.getReceiverName().requestFocus();
            isValid = false;
        }
        if (viewListener.getAddressType().getText().length() == 0) {
            viewListener.setError(viewListener.getAddressTypeLayout(), viewListener.getString(R.string.error_field_required));
            viewListener.getAddressType().requestFocus();
            isValid = false;
        }
        if (viewListener.getSpinnerProvince().getSelectedItemPosition() == 0) {
            viewListener.getSpinnerProvinceError().setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (viewListener.getSpinnerProvince().getSelectedItemPosition() != 0 &&
                viewListener.getSpinnerRegency().getSelectedItemPosition() == 0) {
            viewListener.getSpinnerRegencyError().setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (viewListener.getSpinnerProvince().getSelectedItemPosition() != 0 &&
                viewListener.getSpinnerRegency().getSelectedItemPosition() != 0 &&
                viewListener.getSpinnerSubDistrict().getSelectedItemPosition() == 0) {
            viewListener.getSpinnerSubDistrictError().setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }

    private DataReceiver getDataReceiver(final int action) {
        return new DataReceiver() {

            @Override
            public CompositeSubscription getSubscription() {
                return compositeSubscription;
            }

            @Override
            public void setDistricts(List<District> districts) {
                viewListener.setActionsEnabled(true);
                viewListener.setDistrict(districts);
            }

            @Override
            public void setCities(List<City> cities) {
                viewListener.setActionsEnabled(true);
                viewListener.setCity(cities);
            }

            @Override
            public void setProvinces(List<Province> provinces) {
                viewListener.setActionsEnabled(true);
                viewListener.setProvince(provinces);

            }

            @Override
            public void setBank(List<Bank> banks) {

            }

            @Override
            public void setDepartments(List<CategoryDB> departments) {

            }


            @Override
            public void setShippingCity(List<District> districts) {

            }

            @Override
            public void onNetworkError(String message) {
                viewListener.finishLoading();
                viewListener.showErrorSnackbar(message, onRetryDataManager(action));
            }

            @Override
            public void onMessageError(String message) {
                viewListener.finishLoading();
                viewListener.setActionsEnabled(true);
                viewListener.showErrorSnackbar(message, onRetryDataManager(action));
            }

            @Override
            public void onUnknownError(String message) {
                viewListener.finishLoading();
                viewListener.showErrorSnackbar(message, onRetryDataManager(action));
            }

            @Override
            public void onTimeout() {
                viewListener.finishLoading();
                viewListener.setActionsEnabled(true);
                viewListener.showErrorSnackbar("");
            }

            @Override
            public void onFailAuth() {
                viewListener.finishLoading();
                viewListener.setActionsEnabled(true);
                viewListener.showErrorSnackbar("");
            }

            private View.OnClickListener onRetryDataManager(final int action) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (action) {
                            case GET_LIST_PROVINCE:
                                getListProvince();
                                break;
                            case GET_LIST_DISTRICT:
                                try {
                                    getListDistrict(
                                            ((RegencyAdapter) viewListener.getSpinnerRegency().getAdapter())
                                                    .getList().get(viewListener.getSpinnerRegency()
                                                    .getSelectedItemPosition() - 1));
                                } catch (Exception e) {
                                    Log.d(AddAddressPresenterImpl.this.getClass().getSimpleName(), e.toString());
                                }
                                break;
                            case GET_LIST_CITY:
                                try {
                                    getListCity(
                                            ((ProvinceAdapter) viewListener.getSpinnerProvince().getAdapter())
                                                    .getList().get(viewListener.getSpinnerProvince()
                                                    .getSelectedItemPosition() - 1));
                                } catch (Exception e) {
                                    Log.d(AddAddressPresenterImpl.this.getClass().getSimpleName(), e.toString());
                                }
                                break;
                            default:
                                break;
                        }
                    }

                };
            }

        };
    }
}

