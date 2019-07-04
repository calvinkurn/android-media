package com.tokopedia.shop.open.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.base.list.seller.view.fragment.BasePresenterFragment;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.AppWidgetUtil;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.logisticdata.data.entity.address.DistrictRecommendationAddress;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.seller.LogisticRouter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.PrefixEditText;
import com.tokopedia.shop.open.analytic.ShopOpenTracking;
import com.tokopedia.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.shop.open.util.ShopErrorHandler;
import com.tokopedia.shop.open.view.activity.ShopOpenCreateReadyActivity;
import com.tokopedia.shop.open.view.activity.ShopOpenPostalCodeChooserActivity;
import com.tokopedia.shop.open.view.activity.ShopOpenReserveDomainSuccessActivity;
import com.tokopedia.shop.open.view.activity.ShopOpenWebViewActivity;
import com.tokopedia.shop.open.view.holder.OpenShopAddressViewHolder;
import com.tokopedia.shop.open.view.listener.ShopOpenDomainView;
import com.tokopedia.shop.open.view.presenter.ShopOpenDomainPresenterImpl;
import com.tokopedia.shop.open.view.watcher.AfterTextWatcher;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import javax.inject.Inject;

import static com.tokopedia.core.gcm.Constants.FROM_APP_SHORTCUTS;

/**
 * Created by Hendry on 3/17/2017.
 */

public class ShopOpenReserveDomainFragment extends BasePresenterFragment implements ShopOpenDomainView, OpenShopAddressViewHolder.OpenShopAddressListener {

    public static final int MIN_SHOP_NAME_LENGTH = 3;
    public static final int MIN_SHOP_DOMAIN_LENGTH = 3;
    public static final int REQUEST_CODE_DISTRICTRECOMMENDATION = 1235;
    public static final int REQUEST_CODE_POSTAL_CODE = 1515;
    private static final String EXTRA_DISTRICTRECOMMENDATION = "district_recommendation_address";
    public static final String VALIDATE_DOMAIN_NAME_SHOP = "validate_domain_name_shop";
    public static final String VALIDATE_DOMAIN_SUGGESTION_SHOP = "shop_domain_suggestion";
    public static final String URL_TNC = "https://www.tokopedia.com/terms.pl";
    public static final String URL_PRIVACY_POLICY = "https://www.tokopedia.com/privacy.pl";
    public static final String URL_IMAGE_OPEN_SHOP = "https://ecs7.tokopedia.net/img/android/seller_dashboard_shop/xxhdpi/seller_dashboard.png";
    private static final String SCREEN_NAME = "Nama Toko";
    public static final String OPEN_SHOP_SUBMIT_RAW = "create_open_shop";
    private View buttonSubmit;
    private TkpdHintTextInputLayout textInputShopName;
    private EditText editTextInputShopName;
    private TkpdHintTextInputLayout textInputDomainName;
    private PrefixEditText editTextInputDomainName;
    private SnackbarRetry snackbarRetry;
    private LogisticRouter logisticRouter;
    private OpenShopAddressViewHolder openShopAddressViewHolder;
    private TkpdProgressDialog tkpdProgressDialog;
    RequestParams requestParams;
    private TextView tvTncOpenShop;
    private CheckBox cbTncOpenShop;
    private boolean isTncChecked = false;
    private boolean isDistrictChoosen = false;
    private boolean isPostalCodeChoosen = false;
    private String postalCode;
    private ImageView imgShopOpen;

    @Inject
    ShopOpenDomainPresenterImpl shopOpenDomainPresenter;
    @Inject
    ShopOpenTracking trackingOpenShop;
    @Inject
    UserSessionInterface userSession;

    private boolean fromAppShortCut = false;

    public static ShopOpenReserveDomainFragment newInstance() {
        return new ShopOpenReserveDomainFragment();
    }

    public static ShopOpenReserveDomainFragment newInstance(boolean isFromAppShortcut) {
        Bundle args = new Bundle();
        args.putBoolean(FROM_APP_SHORTCUTS, isFromAppShortcut);
        ShopOpenReserveDomainFragment fragment = new ShopOpenReserveDomainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInjector() {
        ShopOpenDomainComponent component = getComponent(ShopOpenDomainComponent.class);
        component.inject(this);

        shopOpenDomainPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_open_domain, container, false);
        requestParams = RequestParams.create();
        requestParams.putAll((HashMap<String, String>) AuthUtil.generateParams(getActivity()));
        TextView textHello = view.findViewById(R.id.text_hello);
        imgShopOpen = view.findViewById(R.id.img_shop_open);
        buttonSubmit = view.findViewById(R.id.button_submit);
        textInputShopName = view.findViewById(R.id.text_input_shop_name);
        textInputDomainName = view.findViewById(R.id.text_input_domain_name);
        editTextInputShopName = textInputShopName.getEditText();
        editTextInputDomainName = (PrefixEditText) textInputDomainName.getEditText();
        tvTncOpenShop = view.findViewById(R.id.tv_shop_tnc);
        cbTncOpenShop = view.findViewById(R.id.cb_shop_tnc);
        openShopAddressViewHolder = new OpenShopAddressViewHolder(view, getContext(), this);
        String helloName = getString(R.string.hello_x, userSession.getName());
        textHello.setText(MethodChecker.fromHtml(helloName));
        setupTncOpenShop();

        buttonSubmit.setEnabled(false);

        ImageHandler.LoadImage(imgShopOpen, URL_IMAGE_OPEN_SHOP);

        editTextInputShopName.addTextChangedListener(new AfterTextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                textInputShopName.disableSuccessError();
                buttonSubmit.setEnabled(false);
                hideSnackBarRetry();
                if (TextUtils.isEmpty(s)) {
                    textInputShopName.setError(getString(R.string.shop_open_error_shop_name_must_be_filled));
                } else if (s.toString().length() < MIN_SHOP_NAME_LENGTH) {
                    textInputShopName.setError(getString(R.string.shop_open_error_shop_name_min_char));
                } else if (s.toString().length() <= textInputShopName.getCounterMaxLength()) {
                    shopOpenDomainPresenter.checkShop(editTextInputShopName.getText().toString());
                }
            }
        });

        editTextInputDomainName.addTextChangedListener(new AfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (editTextInputDomainName.hasFocus()) {
                    textInputDomainName.disableSuccessError();
                    buttonSubmit.setEnabled(false);
                    hideSnackBarRetry();
                    String domainInputStr = editTextInputDomainName.getTextWithoutPrefix();
                    if (TextUtils.isEmpty(domainInputStr)) {
                        textInputDomainName.setError(getString(R.string.shop_open_error_domain_name_must_be_filled));
                    } else if (domainInputStr.length() < MIN_SHOP_DOMAIN_LENGTH) {
                        textInputDomainName.setError(getString(R.string.shop_open_error_domain_name_min_char));
                    } else if (s.toString().length() <= textInputDomainName.getCounterMaxLength()) {
                        shopOpenDomainPresenter.checkDomain(editTextInputDomainName.getTextWithoutPrefix());
                    }
                }
            }
        });


        cbTncOpenShop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isTncChecked = isChecked;
                checkEnableSubmit();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonSubmitClicked();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        trackingOpenShop.eventMoEngageOpenShop(SCREEN_NAME);
        if (isFromAppShortCut())
            trackingOpenShop.eventSellShortcut();
    }

    private void setupTncOpenShop() {
        SpannableString textTnc = new SpannableString(getString(R.string.tnc_open_shop));
        ClickableSpan tncClickableSpan = setupClickableSpan(URL_TNC, getString(R.string.tnc_webview_title));
        ClickableSpan privacyPolicyClickableSpan = setupClickableSpan(URL_PRIVACY_POLICY, getString(R.string.privacy_policy_webview_title));

        textTnc.setSpan(tncClickableSpan, 21, 42, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textTnc.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 21, 42, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textTnc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tkpd_main_green)), 21, 42, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textTnc.setSpan(privacyPolicyClickableSpan, 48, textTnc.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textTnc.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 48, textTnc.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textTnc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tkpd_main_green)), 48, textTnc.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTncOpenShop.setText(textTnc);
        tvTncOpenShop.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private ClickableSpan setupClickableSpan(String url, String title) {
        return new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(@NotNull View textView) {
                if (getActivity() != null) {
                    Intent intent = ShopOpenWebViewActivity.Companion.newInstance(getActivity(), url, title);
                    startActivity(intent);
                }


            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onAttachListener(Context context) {
        super.onAttachListener(context);
        if (context.getApplicationContext() instanceof LogisticRouter) {
            logisticRouter = (LogisticRouter) context.getApplicationContext();
        }
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        if (arguments != null)
            fromAppShortCut = arguments.getBoolean(FROM_APP_SHORTCUTS);
    }

    @Override
    public void hideSnackBarRetry() {
        if (snackbarRetry != null && snackbarRetry.isShown()) {
            snackbarRetry.hideRetrySnackbar();
        }
    }

    @Override
    public void hideSubmitLoading() {
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }
    }

    @Override
    public void showSubmitLoading() {
        if (tkpdProgressDialog == null) {
            tkpdProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS,
                    getString(R.string.title_loading));
        }
        tkpdProgressDialog.showDialog();
    }

    private void onButtonSubmitClicked() {
        if (!isShopNameDomainValid() || !isTncChecked) {
            buttonSubmit.setEnabled(false);
            return;
        }
        submitCreatingShop();
    }

    @Override
    public boolean isShopNameInValidRange() {
        int inputShopNameLength = editTextInputShopName.getText().length();
        return inputShopNameLength >= MIN_SHOP_NAME_LENGTH && inputShopNameLength <= textInputShopName.getCounterMaxLength();
    }

    @Override
    public boolean isShopDomainInValidRange() {
        int inputShopDomainLength = editTextInputDomainName.getText().length();
        return editTextInputDomainName.getTextWithoutPrefix().length() >= MIN_SHOP_DOMAIN_LENGTH
                && inputShopDomainLength <= textInputDomainName.getCounterMaxLength();
    }

    @Override
    public void onSuccessCheckShopName(boolean existed, String domainSuggestion) {
        if (existed) {
            textInputShopName.setSuccess(getString(R.string.shop_name_available));
            editTextInputDomainName.setText(domainSuggestion);
            textInputDomainName.setSuccess(getString(R.string.domain_name_available));
        } else {
            textInputShopName.setError(getString(R.string.shop_name_not_available));
        }
        checkEnableSubmit();
    }

    @Override
    public void onErrorCheckShopName(String message) {
        textInputShopName.setError(message);
        editTextInputDomainName.setText("");
        textInputDomainName.setSuccess("");
        trackingOpenShop.eventOpenShopBiodataNameError(message);
    }

    @Override
    public void onSuccessCheckShopDomain(boolean existed) {
        if (existed) {
            textInputDomainName.setSuccess(getString(R.string.domain_name_available));
        } else {
            textInputDomainName.setError(getString(R.string.domain_name_not_available));
        }
        checkEnableSubmit();
    }

    @Override
    public void onErrorCheckShopDomain(String message) {
        textInputDomainName.setError(message);
        trackingOpenShop.eventOpenShopBiodataDomainError(message);
    }

    @Override
    public void onErrorReserveShop(Throwable t) {
        hideSubmitLoading();
        String message = ShopErrorHandler.getErrorMessage(getActivity(), t);
        sendErrorTracking(message);
        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(),
                message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        onButtonSubmitClicked();
                    }
                });
        snackbarRetry.showRetrySnackbar();
    }

    private void onErrorSelectPostalCode() {
        String errorMessage = "Pilih Kota Terlebih Dahulu";
        ToasterError.make(getView(), errorMessage, BaseToaster.LENGTH_INDEFINITE)
                .setAction(R.string.title_ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();
    }

    @Override
    public void buttonEnabledFalse() {
        buttonSubmit.setEnabled(false);
    }

    @Override
    public void navigateToDistrictChooser() {
        shopOpenDomainPresenter.openDistrictRecommendation(requestParams);
    }

    @Override
    public void navigateToPostalChooser() {
        // Users have to select district first before select postal code
        if (!isDistrictChoosen) {
            onErrorSelectPostalCode();
        } else {
            if (getActivity() != null) {
                Intent intent = ShopOpenPostalCodeChooserActivity.Companion.createNewInstance(getActivity(), openShopAddressViewHolder.getPostalCode());
                startActivityForResult(intent, REQUEST_CODE_POSTAL_CODE);
            }
        }
    }

    @Override
    public void onSuccessGetToken(Token token) {
        Intent intent = logisticRouter.getDistrictRecommendationIntent(getActivity(), token);
        startActivityForResult(intent, REQUEST_CODE_DISTRICTRECOMMENDATION);
    }

    private void sendErrorTracking(String errorMessage) {
        trackingOpenShop.eventOpenShopBiodataError(errorMessage);
        String generatedErrorMessage = ShopErrorHandler.getGeneratedErrorMessage(errorMessage.toCharArray(),
                editTextInputShopName.getText().toString(), editTextInputDomainName.getTextWithoutPrefix());
        trackingOpenShop.eventOpenShopBiodataErrorWithData(generatedErrorMessage);
    }

    @Override
    public void onSuccessReserveShop(String shopName) {
        hideSubmitLoading();
        trackingOpenShop.eventOpenShopBiodataSuccess();
        goToShopOpenMandatory(shopName);
    }

    @Override
    public void checkEnableSubmit() {
        if (isShopNameDomainValid() && isTncChecked && isDistrictChoosen && isPostalCodeChoosen) {
            buttonSubmit.setEnabled(true);
        } else {
            buttonSubmit.setEnabled(false);
        }
    }

    private boolean isShopNameDomainValid() {
        return isShopNameInValidRange() && isShopDomainInValidRange() &&
                textInputDomainName.isSuccessShown() && textInputShopName.isSuccessShown();
    }

    private void goToShopOpenMandatory(String shopName) {
        Intent intent = ShopOpenReserveDomainSuccessActivity.getIntent(getContext(), shopName);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onSuccessCreateShop(String message, String shopId) {
        hideSubmitLoading();
        AppWidgetUtil.sendBroadcastToAppWidget(getActivity());
        if (getActivity() != null) {
            Intent intent = ShopOpenCreateReadyActivity.Companion.newInstance(getActivity(), shopId);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onErrorCreateShop(String message) {
        hideSubmitLoading();
        ToasterError.make(getView(), message, BaseToaster.LENGTH_INDEFINITE)
                .setAction(R.string.title_ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_POSTAL_CODE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    postalCode = data.getStringExtra(ShopOpenPostalCodeChooserFragment.Companion.getINTENT_DATA_POSTAL_CODE());
                    if (postalCode != null) {
                        isPostalCodeChoosen = true;
                        openShopAddressViewHolder.updatePostalCodeView(postalCode);
                    }
                } break;
            case REQUEST_CODE_DISTRICTRECOMMENDATION:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    DistrictRecommendationAddress address = data.getParcelableExtra(EXTRA_DISTRICTRECOMMENDATION);
                    if (address != null) {
                        isDistrictChoosen = true;
                        clearFocus();
                        openShopAddressViewHolder.setDistrictId(address.getDistrictId());
                        isPostalCodeChoosen = false;
                        openShopAddressViewHolder.updatePostalCodeView(null);
                        openShopAddressViewHolder.initPostalCode(address.getZipCodes());
                        openShopAddressViewHolder.updateLocationView(
                                address.getProvinceName(),
                                address.getCityName(),
                                address.getDistrictName()
                        );
                        openShopAddressViewHolder.updatePostalCodeView("");
                    }
                } break;
        }
    }

    private void submitCreatingShop() {
        showSubmitLoading();
        String shopName = editTextInputShopName.getText().toString().trim();
        String shopDomain = editTextInputDomainName.getTextWithoutPrefix().trim();
        Integer districtId = openShopAddressViewHolder.getDistrictId();
        Integer postalCodeId = Integer.valueOf(postalCode);
        shopOpenDomainPresenter.onSubmitCreateShop(shopName, shopDomain, districtId, postalCodeId);
    }

    public void clearFocus() {
        openShopAddressViewHolder.clearFocus();
        editTextInputDomainName.clearFocus();
        editTextInputShopName.clearFocus();
    }

    public boolean isFromAppShortCut() {
        return fromAppShortCut;
    }
}