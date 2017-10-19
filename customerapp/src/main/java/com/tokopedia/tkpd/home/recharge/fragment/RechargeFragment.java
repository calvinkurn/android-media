package com.tokopedia.tkpd.home.recharge.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.customView.RechargeEditText;
import com.tokopedia.core.database.model.RechargeOperatorModel;
import com.tokopedia.core.database.model.category.Category;
import com.tokopedia.core.database.model.category.CategoryAttributes;
import com.tokopedia.core.database.model.category.ClientNumber;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.OldSessionRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.VersionInfo;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.fragment.FragmentIndexCategory;
import com.tokopedia.tkpd.home.recharge.activity.RechargePaymentWebView;
import com.tokopedia.tkpd.home.recharge.adapter.NominalAdapter;
import com.tokopedia.tkpd.home.recharge.adapter.OperatorAdapter;
import com.tokopedia.tkpd.home.recharge.presenter.RechargePresenter;
import com.tokopedia.tkpd.home.recharge.presenter.RechargePresenterImpl;
import com.tokopedia.tkpd.home.recharge.util.OperatorComparator;
import com.tokopedia.tkpd.home.recharge.util.ProductComparator;
import com.tokopedia.tkpd.home.recharge.view.RechargeView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


/**
 * @author kulomady 05 on 7/11/2016.
 */
@RuntimePermissions
public class RechargeFragment extends Fragment implements RechargeEditText.RechargeEditTextListener,
        RechargeEditText.OnButtonPickerListener,
        RechargeView, AdapterView.OnItemSelectedListener,
        CompoundButton.OnCheckedChangeListener, View.OnFocusChangeListener, View.OnTouchListener {
    private static final String EXTRA_CHECKOUT_PASS_DATA = "EXTRA_CHECKOUT_PASS_DATA";

    //region final static member variable
    private static final String TAG = "RechargeFragment";
    private static final int CONTACT_PICKER_RESULT = 1001;
    private static final String ARG_PARAM_CATEGORY = "ARG_PARAM_CATEGORY";
    private static final String ARG_TAB_INDEX_POSITION = "ARG_TAB_INDEX_POSITION";

    public static final String ARG_UTM_SOURCE = "ARG_UTM_SOURCE";
    public static final String ARG_UTM_MEDIUM = "ARG_UTM_MEDIUM";
    public static final String ARG_UTM_CAMPAIGN = "ARG_UTM_CAMPAIGN";
    public static final String ARG_UTM_CONTENT = "ARG_UTM_CONTENT";
    private final static int OUT_OF_STOCK = 3;
    private static final String LAST_INPUT_KEY = "lastInputKey";
    private static final int LOGIN_REQUEST_CODE = 198;
    private static final String KEY_PHONEBOOK = "phoneBook";
    private static final int TOTAL_RADIO_BUTTON_OPTION = 2;

    //endregion

    //region widget variable
    @BindView(R.id.pulsa_edittext)
    RechargeEditText rechargeEditText;
    @BindView(R.id.buy_with_credit_checkbox)
    CheckBox buyWithCreditCheckbox;
    @BindView(R.id.buy_wrapper_linearlayout)
    LinearLayout wrapperLinearLayout;
    @BindView(R.id.nominalTextview)
    TextView nominalTextview;
    @BindView(R.id.telp_textview)
    TextView tlpLabelTextView;
    @BindView(R.id.recharge_progressbar)
    ProgressBar rechargeProgressbar;
    @BindView(R.id.spnNominal)
    Spinner spnNominal;
    @BindView(R.id.spnOperator)
    Spinner spnOperator;
    @BindView(R.id.errorNominal)
    TextView errorNominal;
    @BindView(R.id.btn_buy)
    Button buyButton;
    @BindView(R.id.radio_group_container)
    LinearLayout radGroupContainer;
    @BindView(R.id.layout_checkout)
    RelativeLayout layoutCheckout;
    @BindView(R.id.tooltip_instant_checkout)
    ImageView tooltipInstantCheckout;

    //endregion

    //region member variable
    private RechargePresenter rechargePresenter;
    private boolean isAlreadyHavePhonePrefixInView;
    private Category category;
    private List<Product> productList;
    private List<RechargeOperatorModel> operatorList;
    private Product selectedProduct;
    private RechargeOperatorModel selectedOperator;
    private String selectedOperatorId;
    private LocalCacheHandler cacheHandlerPhoneBook;
    private LastOrder lastOrder;
    private int minLengthDefaultOperator = -1;
    private Bundle bundle;
    private Boolean showPrice = true;
    private int currentPosition;
    private Unbinder unbinder;
    private RadioGroup radGroup;
    private LocalCacheHandler localCacheHandlerLastClientNumber;
    private BottomSheetView bottomSheetView;
    //endregion

    private String lastClientNumberTyped = "";
    private String lastOperatorSelected = "";
    private String lastProductSelected = "";

    private DigitalCheckoutPassData digitalCheckoutPassDataState;

    public static RechargeFragment newInstance(Category category, int position) {
        RechargeFragment rechargeFragment = new RechargeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PARAM_CATEGORY, category);
        bundle.putInt(ARG_TAB_INDEX_POSITION, position);
        rechargeFragment.setArguments(bundle);
        return rechargeFragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        setVisibilityImageAndProductView(isVisibleToUser);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null)
            digitalCheckoutPassDataState = savedInstanceState.getParcelable(
                    EXTRA_CHECKOUT_PASS_DATA
            );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_CHECKOUT_PASS_DATA, digitalCheckoutPassDataState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Contact contact;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    try {
                        Uri contactURI = intent.getData();
                        contact = fetchAndBuildContact(getActivity(), contactURI);
                        getPhoneNumberAndDisplayIt(contact);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL:
                    if (intent != null && intent.hasExtra(IDigitalModuleRouter.EXTRA_MESSAGE)) {
                        String message = intent.getStringExtra(IDigitalModuleRouter.EXTRA_MESSAGE);
                        if (!TextUtils.isEmpty(message)) {
                            NetworkErrorHelper.showSnackbar(getActivity(), message);
                        }
                    }
                    break;
                case LOGIN_REQUEST_CODE:
                    if (SessionHandler.isV4Login(getActivity()) && digitalCheckoutPassDataState != null) {
                        if (getActivity().getApplication() instanceof IDigitalModuleRouter) {
                            IDigitalModuleRouter digitalModuleRouter = (IDigitalModuleRouter) getActivity().getApplication();
                            startActivityForResult(
                                    digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassDataState),
                                    IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL
                            );
                        }
                    }
                    break;
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    //region android.support.fragment implementation / lifecycle
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);
        if (this.getArguments() != null) {
            Bundle bundle = this.getArguments();
            category = bundle.getParcelable(ARG_PARAM_CATEGORY);
            currentPosition = bundle.getInt(ARG_TAB_INDEX_POSITION);
            rechargePresenter = new RechargePresenterImpl(getContext(), this);
            setupArguments(getArguments());
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recharge, container, false);
        try {
            unbinder = ButterKnife.bind(this, view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initListener();
        CategoryAttributes categoryAttributes = category.getAttributes();

        lastClientNumberTyped = getLastClientNumberTyped(String.valueOf(category.getId()));
        lastOperatorSelected = getLastOperatorSelected(String.valueOf(category.getId()));
        lastProductSelected = getLastProductSelected(String.valueOf(category.getId()));

        rechargePresenter.fetchRecentNumbers(category.getId());
        hideProgressFetchData();
        setRechargeEditTextCallback();
        setRechargeEditTextTouchCallback();
        renderDefaultView(categoryAttributes);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setRechargeEditTextCallback() {
        if (rechargeEditText != null) {
            rechargeEditText.getAutoCompleteTextView().setOnFocusChangeListener(this);
        }
    }

    private void setRechargeEditTextTouchCallback() {
        if (rechargeEditText != null) {
            rechargeEditText.getAutoCompleteTextView().setOnTouchListener(this);
        }
    }

    private void removeRechargeEditTextCallback() {
        if (rechargeEditText != null) {
            rechargeEditText.getAutoCompleteTextView().setOnFocusChangeListener(null);
        }
    }


    @Override
    public void onDestroy() {
        rechargePresenter.clearRechargePhonebookCache();
        removeRechargeEditTextCallback();
        unbinder.unbind();
        super.onDestroy();
    }

    //endregion

    //region widget listener implementation
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedProduct = productList.get(position);
        checkStockProduct(selectedProduct);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onRechargeTextChanged(CharSequence s, int start, int before, int count) {
        String temp = s.toString();
        temp = validateTextPrefix(temp);
        if (temp.length() <= 4) {
            if (isDeleteChar(before, count)) {
                isAlreadyHavePhonePrefixInView = false;
                hideFormAndImageOperator();
            }
        }

        if (!isValidatePrefix()) {
            if (s.length() >= minLengthDefaultOperator) {
                if (isOperatorShowProduct()) {
                    this.rechargePresenter.validateWithOperator(
                            category.getId(), selectedOperatorId);
                } else {
                    if (selectedOperatorId == null) {
                        selectedOperatorId = category.getAttributes().getDefaultOperatorId();
                    } else {
                        this.rechargePresenter.validateOperatorWithoutProduct(category.getId(),
                                selectedOperatorId);
                    }
                    hideFormAndShowImageOperator();
                }
            } else {
                isAlreadyHavePhonePrefixInView = false;
                hideFormAndImageOperator();
            }
        } else {
            if (temp.length() >= 3) {
                if (s.length() >= 3) {
                    this.rechargePresenter.validatePhonePrefix(temp, category.getId(),
                            category.getAttributes().isValidatePrefix());
                } else {
                    isAlreadyHavePhonePrefixInView = false;
                    hideFormAndImageOperator();
                }
            } else {
                isAlreadyHavePhonePrefixInView = false;
                hideFormAndImageOperator();
            }
        }
        if (s.length() == 0) {
            isAlreadyHavePhonePrefixInView = false;
            setPhoneBookVisibility();
            hideFormAndImageOperator();
        }
    }

    @Override
    public void onRechargeTextClear() {
        //TODO ini yang lama
        /*LocalCacheHandler.clearCache(getActivity(), KEY_PHONEBOOK);*/
        rechargePresenter.clearRechargePhonebookCache();
        hideFormAndImageOperator();
    }

    private boolean isValidatePrefix() {
        return category.getAttributes().isValidatePrefix();
    }

    private boolean isOperatorShowProduct() {
        return selectedOperator != null && selectedOperator.showProduct;
    }

    private Boolean isDeleteChar(int before, int count) {
        return before == 1 && count == 0;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        buyButton.setText(
                isChecked ? getResources().getString(R.string.title_button_pay)
                        : getResources().getString(R.string.title_buy)
        );

        UnifyTracking.eventCheckInstantSaldoWidget(category.getAttributes().getName(),
                selectedOperator == null ? "" : selectedOperator.name, isChecked);
    }

    @Override
    public void onButtonContactClicked() {
        RechargeFragmentPermissionsDispatcher.doLaunchContactPickerWithCheck(RechargeFragment.this);
        UnifyTracking.eventClickPhoneIcon(category.getAttributes().getName(), selectedOperator == null ? "" : selectedOperator.name);
    }

    @OnClick(R.id.btn_buy)
    void buttonBuyClicked() {

        if (SessionHandler.isV4Login(getActivity())) {
            sendGTMClickBeli();

            if (selectedProduct == null) {
                rechargePresenter.getDefaultProduct(String.valueOf(category.getId()),
                        selectedOperatorId, String.valueOf(selectedOperator.defaultProductId));
            } else {
                if (checkStockProduct(selectedProduct))
//                    goToCheckout(getUrlCheckout());
                    goToNativeCheckout();
            }
        } else {
            if (selectedProduct == null) {
                rechargePresenter.getDefaultProduct(String.valueOf(category.getId()),
                        selectedOperatorId, String.valueOf(selectedOperator.defaultProductId));
            } else {
                gotoLogin();
            }
        }
    }
    //endregion

    //region view implementation
    @Override
    public void showProgressFetchingData() {
        this.rechargeEditText.setVisibility(View.GONE);
        this.rechargeProgressbar.setVisibility(View.VISIBLE);
        this.tlpLabelTextView.setVisibility(View.GONE);
    }

    @Override
    public void renderDataProducts(List<Product> productList) {
        Collections.sort(productList, new ProductComparator());
        if (isRechargeEditTextFilled() ||
                !category.getAttributes().getClientNumber().isShown()) {
            if (productList.size() > 0) {
                this.productList = productList;
                isAlreadyHavePhonePrefixInView = true;
                NominalAdapter adapter = new NominalAdapter(
                        getActivity(),
                        android.R.layout.simple_spinner_item,
                        productList,
                        this.showPrice
                );
                spnNominal.setAdapter(adapter);
                spnNominal.setOnItemSelectedListener(this);
                spnNominal.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            UnifyTracking.eventSelectProductWidget(category.getAttributes().getName(), selectedProduct.getAttributes().getPrice());
                        }
                        return false;
                    }
                });
                setSpnNominalSelectionBasedStatus(productList);

                setSpnNominalSelectionBasedLastOrder(productList);

                checkStockProduct(productList.get(spnNominal.getSelectedItemPosition()));
                showFormAndImageOperator();
            } else {
                this.hideFormAndImageOperator();
            }
        } else {
            hideFormAndImageOperator();
            isAlreadyHavePhonePrefixInView = false;
        }
    }

    private boolean isRechargeEditTextFilled() {
        return rechargeEditText.getText().length() >= 0 && !rechargeEditText.getText().trim().equals("");
    }

    @Override
    public void renderDataOperators(List<RechargeOperatorModel> operators) {
        Collections.sort(operators, new OperatorComparator());
        if (operators.size() <= TOTAL_RADIO_BUTTON_OPTION) {
            addRadioButtonOperator(operators);
        } else {
            operatorList = operators;
            spnOperator.setVisibility(View.VISIBLE);
            OperatorAdapter adapterOperator = new OperatorAdapter(
                    getActivity(),
                    android.R.layout.simple_spinner_item,
                    operators
            );
            spnOperator.setAdapter(adapterOperator);
            spnOperator.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        UnifyTracking.eventSelectProductWidget(category.getAttributes().getName(),
                                selectedOperator == null ? "" : selectedOperator.name);
                    }
                    return false;
                }
            });
            spnOperator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    hideFormAndImageOperator();
                    selectedOperator = operatorList.get(i);
                    setInputTypeEditTextRecharge(selectedOperator.allowAlphanumeric);
                    selectedOperatorId = String.valueOf((selectedOperator.operatorId));
                    setInitialClientNumberAfterOperatorSelection();
                    if (!category.getAttributes().getClientNumber().isShown()) {
                        setUpForNotUsingTextEdit();
                    } else {
                        rechargePresenter.updateMinLenghAndOperator(selectedOperatorId);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            if (SessionHandler.isV4Login(getActivity()) && lastOrder != null
                    && lastOrder.getData().getAttributes().getCategory_id() == category.getId()) {
                for (int i = 0, operatorsSize = operators.size(); i < operatorsSize; i++) {
                    RechargeOperatorModel model = operators.get(i);
                    if (String.valueOf(model.operatorId)
                            .equalsIgnoreCase(
                                    String.valueOf(lastOrder.getData().getAttributes().getOperator_id()
                                    ))) {
                        spnOperator.setSelection(i);
                    }
                }
            } else {
                for (int i = 0, operatorsSize = operators.size(); i < operatorsSize; i++) {
                    RechargeOperatorModel model = operators.get(i);
                    if (String.valueOf(model.operatorId).equalsIgnoreCase(lastOperatorSelected)) {
                        spnOperator.setSelection(i);
                    }
                }
            }
        }
    }

    private void setInitialClientNumberAfterOperatorSelection() {
        if (SessionHandler.isV4Login(getActivity()) && lastOrder != null
                && !TextUtils.isEmpty(lastOrder.getData().getAttributes().getClient_number())) {
            if (lastOrder.getData().getAttributes().getCategory_id() == category.getId() &&
                    lastOrder.getData().getAttributes().getOperator_id() == Integer.parseInt(selectedOperatorId)) {
                rechargeEditText.setText(lastOrder.getData().getAttributes().getClient_number());
            } else {
                rechargeEditText.setEmptyString();
            }
        } else if (!lastClientNumberTyped.isEmpty() && lastOperatorSelected.equals(selectedOperatorId)) {
            rechargeEditText.setText(lastClientNumberTyped);
        } else {
            rechargeEditText.setEmptyString();
        }
    }

    private void setSpnNominalSelectionBasedLastOrder(List<Product> productList) {
        if (SessionHandler.isV4Login(getActivity())
                && lastOrder != null && lastOrder.getData() != null
                && lastOrder.getData().getAttributes() != null) {
            int lastProductId = lastOrder.getData().getAttributes().getProduct_id();
            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i).getId() == (lastProductId)) {
                    spnNominal.setSelection(i);
                }
            }
        } else {
            for (int i = 0; i < productList.size(); i++) {
                if (String.valueOf(productList.get(i).getId())
                        .equalsIgnoreCase(lastProductSelected)) {
                    spnNominal.setSelection(i);
                }
            }
        }
    }

    private void checkRadioButtonBasedOnLastOrder(List<RechargeOperatorModel> operators, RadioGroup radioGroup) {
        if (lastOrder != null && lastOrder.getData() != null
                && lastOrder.getData().getAttributes() != null && radioGroup != null) {
            for (int i = 0; i < operators.size(); i++) {
                if (operators.get(i).operatorId == lastOrder.getData().getAttributes().getOperator_id()) {
                    radioGroup.check(radioGroup.getChildAt(i).getId());
                    selectedOperator = operators.get(radGroup.getChildAt(i).getId());
                    rechargePresenter.validateWithOperator(
                            category.getId(), String.valueOf(selectedOperator.operatorId));
                }
            }
        } else {
            if (radioGroup != null)
                for (int i = 0; i < operators.size(); i++) {
                    if (String.valueOf(operators.get(i).operatorId)
                            .equalsIgnoreCase(lastOperatorSelected)) {
                        radioGroup.check(radioGroup.getChildAt(i).getId());
                        selectedOperator = operators.get(radGroup.getChildAt(i).getId());
                        rechargePresenter.validateWithOperator(
                                category.getId(), String.valueOf(selectedOperator.operatorId));
                    }
                }
        }
    }

    private void setSpnNominalSelectionBasedStatus(List<Product> productList) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getAttributes().getStatus() != OUT_OF_STOCK) {
                spnNominal.setSelection(i);
                break;
            }
        }
    }

    private void hideFormAndShowImageOperator() {
        if (spnNominal != null) spnNominal.setVisibility(View.GONE);
        if (wrapperLinearLayout != null) wrapperLinearLayout.setVisibility(View.VISIBLE);
        if (nominalTextview != null) nominalTextview.setVisibility(View.GONE);
        if (rechargeEditText != null) rechargeEditText.setImgOperatorVisible();
        if (errorNominal != null) errorNominal.setVisibility(View.GONE);
    }

    @Override
    public void renderDataProductsEmpty(String message) {
        isAlreadyHavePhonePrefixInView = false;
        this.hideFormAndImageOperator();
    }

    @Override
    public void showImageOperator(String imageUrl) {
        this.rechargeEditText.setImgOperator(imageUrl);
    }

    @Override
    public void setOperatorView(RechargeOperatorModel operator) {
        try {
            selectedOperator = operator;
            this.minLengthDefaultOperator = operator.minimumLength;
            this.rechargeEditText.getAutoCompleteTextView().setFilters(
                    new InputFilter[]{new InputFilter.LengthFilter(operator.maximumLength)}
            );
            if (operator.nominalText != null && operator.nominalText.length() > 0)
                this.nominalTextview.setText(operator.nominalText);
            if (!operator.showProduct) {
                this.spnNominal.setVisibility(View.GONE);
                this.nominalTextview.setVisibility(View.GONE);
            }
            if (!operator.showPrice) {
                this.showPrice = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showProductById(Product product) {
        selectedProduct = product;
        if (SessionHandler.isV4Login(getActivity())) {
            if (checkStockProduct(selectedProduct))
                goToNativeCheckout();
        } else {
            gotoLogin();
        }
    }

    @Override
    public void hideProgressFetchData() {
        this.rechargeProgressbar.setVisibility(View.GONE);
        this.rechargeEditText.setVisibility(View.VISIBLE);
        this.tlpLabelTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFormAndImageOperator() {
        if (spnNominal != null) spnNominal.setVisibility(View.GONE);
        if (wrapperLinearLayout != null) wrapperLinearLayout.setVisibility(View.GONE);
        if (nominalTextview != null) nominalTextview.setVisibility(View.GONE);
        if (rechargeEditText != null) rechargeEditText.setImgOperatorInvisible();
        if (errorNominal != null) errorNominal.setVisibility(View.GONE);
    }

    @Override
    public void showFormAndImageOperator() {
        if (rechargeEditText != null) rechargeEditText.setImgOperatorVisible();
        if (spnNominal != null) spnNominal.setVisibility(View.VISIBLE);
        if (wrapperLinearLayout != null) wrapperLinearLayout.setVisibility(View.VISIBLE);
        if (nominalTextview != null) nominalTextview.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderDataRecent(List<String> results) {
        if (SessionHandler.isV4Login(getActivity())) {
            rechargeEditText.setDropdownAutoComplete(results);
        }
        if (isAlreadyHavePhonePrefixInView) {
            showFormAndImageOperator();
        } else {
            hideFormAndImageOperator();
        }
        setPhoneBookVisibility();
    }
    //endregion

    //region view helper
    private void initListener() {
        rechargeEditText.setRechargeEditTextListener(this);
        rechargeEditText.setButtonPickerListener(this);
    }

    private void renderDefaultView(CategoryAttributes categoryAttributes) {
        ClientNumber clientNumber = categoryAttributes.getClientNumber();
        tlpLabelTextView.setText(clientNumber.getText());
        rechargeEditText.setHint(clientNumber.getPlaceholder());
        renderInstantCheckoutOption(categoryAttributes.isInstantCheckoutAvailable());

        setTextToEditTextOrSetVisibilityForm();
        setPhoneBookVisibility();
        setBottomSheetDialog();

        if (!category.getAttributes().isValidatePrefix()) {
            selectedOperatorId = category.getAttributes().getDefaultOperatorId();
            if (!category.getAttributes().getClientNumber().isShown()) {
                this.rechargePresenter.updateMinLenghAndOperator(selectedOperatorId);
                setUpForNotUsingTextEdit();
            }

            if (category.getAttributes().isShowOperator()) {
                this.rechargePresenter.getListOperatorFromCategory(category.getId());
            }
        }
    }

    private void setBottomSheetDialog() {
        bottomSheetView = new BottomSheetView(getActivity());
        bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(getActivity().getString(com.tokopedia.digital.R.string.title_tooltip_instan_payment))
                .setBody(getActivity().getString(com.tokopedia.digital.R.string.body_tooltip_instan_payment))
                .setImg(com.tokopedia.digital.R.drawable.ic_digital_instant_payment)
                .build());
    }

    private void renderInstantCheckoutOption(boolean isInstantCheckoutAvailable) {
        if (isInstantCheckoutAvailable) {
            layoutCheckout.setVisibility(View.VISIBLE);
            buyWithCreditCheckbox.setOnCheckedChangeListener(this);
            buyWithCreditCheckbox.setChecked(
                    rechargePresenter.isRecentInstantCheckoutUsed(String.valueOf(category.getId())));
        } else {
            buyWithCreditCheckbox.setChecked(false);
            layoutCheckout.setVisibility(View.GONE);
        }
        tooltipInstantCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetView.show();
            }
        });
    }

    private void setInputTypeEditTextRecharge(boolean allowAlphanumeric) {
        if (allowAlphanumeric) {
            rechargeEditText.setInputTypeText();
        } else {
            rechargeEditText.setInputTypeNumber();
        }
    }

    private void setUpForNotUsingTextEdit() {
        tlpLabelTextView.setVisibility(View.GONE);
        rechargeEditText.setVisibility(View.GONE);
        rechargeEditText.setEmptyString();
        this.rechargePresenter.validateWithOperator(
                category.getId(),
                selectedOperatorId);
    }

    private void addRadioButtonOperator(final List<RechargeOperatorModel> operators) {
        spnOperator.setVisibility(View.GONE);
        radGroupContainer.setVisibility(View.VISIBLE);

        radGroup = new RadioGroup(getActivity());
        radGroupContainer.addView(radGroup);
        radGroup.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < operators.size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setId(i);
            radioButton.setText(operators.get(i).name);
            radioButton.setTextSize(getResources().getDimension(R.dimen.text_size_small) /
                    getResources().getDisplayMetrics().density);
            radioButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey_600));
            radGroup.addView(radioButton);
        }
        radGroup.check(radGroup.getChildAt(0).getId());
        selectedOperator = operators.get(radGroup.getChildAt(0).getId());
        setInputTypeEditTextRecharge(selectedOperator.allowAlphanumeric);
        checkRadioButtonBasedOnLastOrder(operators, radGroup);
        selectedOperatorId = String.valueOf((selectedOperator.operatorId));
        minLengthDefaultOperator = selectedOperator.minimumLength;
        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                hideFormAndImageOperator();
                selectedProduct = null;
                selectedOperator = operators.get(i);
                selectedOperatorId = String.valueOf((operators.get(i).operatorId));
                minLengthDefaultOperator = selectedOperator.minimumLength;
                setInputTypeEditTextRecharge(selectedOperator.allowAlphanumeric);
                setInitialClientNumberAfterOperatorSelection();
            }
        });
    }

    private void setTextToEditTextOrSetVisibilityForm() {
        cacheHandlerPhoneBook = new LocalCacheHandler(getActivity(), KEY_PHONEBOOK);

        String defaultPhoneNumber = SessionHandler.getPhoneNumber();

        if (SessionHandler.isV4Login(getActivity())
                && rechargePresenter.isAlreadyHaveLastOrderDataOnCacheByCategoryId(category.getId())) {
            renderLastOrder();
        } else if (SessionHandler.isV4Login(getActivity())
                && !rechargePresenter.isAlreadyHaveLastOrderDataOnCacheByCategoryId(category.getId())
                && !TextUtils.isEmpty(lastClientNumberTyped)
                && lastOperatorSelected.equals(selectedOperatorId)) {
            rechargeEditText.setText(lastClientNumberTyped);
            handlingAppearanceFormAndImageOperator();
        } else if (SessionHandler.isV4Login(getActivity())
                && !rechargePresenter.isAlreadyHaveLastOrderDataOnCacheByCategoryId(category.getId())
                && TextUtils.isEmpty(lastClientNumberTyped)
                && (category.getId() == 1 || category.getId() == 2)
                && !TextUtils.isEmpty(defaultPhoneNumber)) {
            rechargeEditText.setText(defaultPhoneNumber);
            handlingAppearanceFormAndImageOperator();
        } else if (!SessionHandler.isV4Login(getActivity())
                && !TextUtils.isEmpty(lastClientNumberTyped)) {
            rechargeEditText.setText(lastClientNumberTyped);
            handlingAppearanceFormAndImageOperator();
        }
    }

    private void renderLastOrder() {
        lastOrder = rechargePresenter.getLastOrderFromCache();
        if (lastOrder != null && lastOrder.getData() != null && category != null) {
            if (lastOrder.getData().getAttributes().getCategory_id() == category.getId()) {
                rechargeEditText.setText(lastOrder.getData().getAttributes().getClient_number());
            }
            handlingAppearanceFormAndImageOperator();
        }
    }

    private void handlingAppearanceFormAndImageOperator() {
        if (rechargeEditText != null && !rechargeEditText.getText().trim().equals("")) {
            if (isAlreadyHavePhonePrefixInView) {
                showFormAndImageOperator();
            } else {
                if (selectedOperator != null && selectedOperator.showProduct) {
                    showFormAndImageOperator();
                } else {
                    hideFormAndShowImageOperator();
                }
            }
        }
    }

    private void setPhoneBookVisibility() {
        if (category != null && category.getAttributes() != null) {
            CategoryAttributes categoryAttributes = category.getAttributes();
            if (categoryAttributes.isUsePhonebook() && rechargeEditText != null) {

                rechargeEditText.getBtnContactPicker().setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                layoutParams.weight = 0.92f;
                rechargeEditText.getPulsaFramelayout().setLayoutParams(layoutParams);
            } else if (rechargeEditText != null) {
                rechargeEditText.getBtnContactPicker().setVisibility(View.GONE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                layoutParams.weight = 1;
                rechargeEditText.getPulsaFramelayout().setLayoutParams(layoutParams);
            }
        }
    }

    private void setVisibilityImageAndProductView(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (isAlreadyHavePhonePrefixInView) {
                setTextToEditTextOrSetVisibilityForm();
            } else {
                handlingAppearanceFormAndImageOperator();
            }
            setPhoneBookVisibility();

        } else {
            hideKeyboard();
        }
    }

    private void hideKeyboard() {
        if (getView() != null) {
            InputMethodManager inputManager = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(
                    getView().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS
            );
        }
    }

    private boolean checkStockProduct(Product product) {
        boolean isAvailable = product.getAttributes().getStatus() != OUT_OF_STOCK;
        if (isAvailable) {
            errorNominal.setVisibility(View.GONE);
        } else {
            errorNominal.setVisibility(View.VISIBLE);
            errorNominal.setText(R.string.title_empty_stock);
        }
        return isAvailable;
    }

    //endregion

    //region contact
    private void getPhoneNumberAndDisplayIt(Contact contact) {
        String phoneNumber = contact.contactNumber;
        phoneNumber = validateTextPrefix(phoneNumber);
        rechargeEditText.setText(phoneNumber);
        rechargePresenter.saveLastInputToCache(LAST_INPUT_KEY + category.getId(), rechargeEditText.getText());
        cacheHandlerPhoneBook.putString(KEY_PHONEBOOK + category.getId(), phoneNumber);
        cacheHandlerPhoneBook.applyEditor();
    }

    private String validateTextPrefix(String phoneNumber) {
        if (phoneNumber.startsWith("62")) {
            phoneNumber = phoneNumber.replaceFirst("62", "0");
        }
        if (phoneNumber.startsWith("+62")) {
            phoneNumber = phoneNumber.replace("+62", "0");
        }
        phoneNumber = phoneNumber.replace(".", "");

        return phoneNumber.replaceAll("[^0-9]+", "");
    }

    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    public void doLaunchContactPicker() {
        storeLastStateTabSelected();
        Intent contactPickerIntent = new Intent(
                Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        );
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }


    private Contact fetchAndBuildContact(Context ctx, Uri uriContact) {
        String id = uriContact.getLastPathSegment();
        Contact contact = new Contact();
        contact = buildContactPhoneDetails(ctx, contact, id);
        return contact;
    }

    private Contact buildContactPhoneDetails(Context ctx, final Contact contact, String id) {
        ContentResolver contentResolver = ctx.getContentResolver();
        String contactWhere = ContactsContract.CommonDataKinds.Phone._ID + " = ? AND "
                + ContactsContract.Data.MIMETYPE + " = ?";

        String[] contactWhereParams = new String[]{
                id,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
        };
        Cursor cursorPhone = contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                null,
                contactWhere,
                contactWhereParams,
                null
        );

        mappingCursorToObjectContact(contact, cursorPhone);
        return contact;
    }

    private void mappingCursorToObjectContact(Contact contact, Cursor cursorPhone) {
        if (cursorPhone != null) {
            if (cursorPhone.getCount() > 0) {
                if (cursorPhone.moveToNext()) {
                    if (Integer.parseInt(cursorPhone.getString(
                            cursorPhone.getColumnIndex(
                                    ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0
                            ) {
                        String givenName = cursorPhone.getString(
                                cursorPhone.getColumnIndex(
                                        ContactsContract.Contacts.DISPLAY_NAME
                                )
                        );

                        int contactType = cursorPhone.getInt(
                                cursorPhone.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.TYPE
                                )
                        );
                        contact.contactNumber = cursorPhone.getString(
                                cursorPhone.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER
                                )
                        );
                        contact.givenName = givenName;
                        contact.contactType = contactType;
                    }
                    cursorPhone.moveToNext();
                }
            }
            cursorPhone.close();
        }
    }

    //region navigation
    private void goToCheckout(String url) {
        Intent intent = new Intent(getActivity(), RechargePaymentWebView.class);
        intent.putExtra("url", url);
        rechargePresenter.clearRechargePhonebookCache();
        LocalCacheHandler.clearCache(getActivity(), KEY_PHONEBOOK);
        startActivity(intent);
    }


    //endregion

    private void gotoLogin() {
        digitalCheckoutPassDataState = getGeneratedCheckoutPassData(rechargeEditText.getText());

        Intent intent = OldSessionRouter.getLoginActivityIntent(getActivity());
        intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
        rechargePresenter.saveLastInputToCache(
                LAST_INPUT_KEY + category.getId(),
                rechargeEditText.getText()
        );

        storeLastClientNumberTyped(String.valueOf(category.getId()), rechargeEditText.getText());

        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    private String getUrlCheckout() {
        String clientNumber = rechargeEditText.getText();
        String url = TkpdBaseURL.PULSA_WEB_DOMAIN + "?" +
                "action=init_data" +
                "&client_number=" + clientNumber +
                "&product_id=" + selectedProduct.getId() +
                "&operator_id=" + selectedProduct.getRelationships().getOperator().getData().getId() +
                "&is_promo=" + (selectedProduct.getAttributes().getPromo() != null ? "1" : "0") +
                "&atoken=" + generateATokenRechargeCheckout() +
                "&instant_checkout=" + (buyWithCreditCheckbox.isChecked() ? "1" : "0") +
                "&utm_source=" + bundle.getString(ARG_UTM_SOURCE, "android") +
                "&utm_medium=" + bundle.getString(ARG_UTM_MEDIUM, "widget") +
                "&utm_campaign=" + bundle.getString(ARG_UTM_CAMPAIGN, category.getAttributes().getName()) +
                "&utm_content=" + bundle.getString(ARG_UTM_CONTENT, VersionInfo.getVersionInfo(getActivity()));

        Log.i("RECHARGE", "PULSA URL: " + Uri.encode(url) + "\n RAW : " + url);

        return URLGenerator.generateURLSessionLogin(Uri.encode(url), getActivity());
    }

    private void goToNativeCheckout() {
        rechargePresenter.storeLastInstantCheckoutUsed(String.valueOf(category.getId()),
                buyWithCreditCheckbox.isChecked());

        String clientNumber = rechargeEditText.getText();
        DigitalCheckoutPassData digitalCheckoutPassData = getGeneratedCheckoutPassData(clientNumber);
        if (getActivity().getApplication() instanceof IDigitalModuleRouter) {
            IDigitalModuleRouter digitalModuleRouter = (IDigitalModuleRouter) getActivity().getApplication();
            startActivityForResult(
                    digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassData),
                    IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL
            );
        }

        storeLastClientNumberTyped(String.valueOf(category.getId()), clientNumber);
    }

    @NonNull
    private DigitalCheckoutPassData getGeneratedCheckoutPassData(String clientNumber) {
        return new DigitalCheckoutPassData.Builder()
                .action("init_data")
                .categoryId(String.valueOf(category.getId()))
                .clientNumber(clientNumber)
                .instantCheckout(buyWithCreditCheckbox.isChecked() ? "1" : "0")
                .isPromo(selectedProduct.getAttributes().getPromo() != null ? "1" : "0")
                .operatorId(
                        String.valueOf(selectedProduct.getRelationships().getOperator()
                                .getData().getId())
                )
                .productId(String.valueOf(selectedProduct.getId()))
                .utmCampaign(bundle.getString(ARG_UTM_CAMPAIGN, category.getAttributes().getName()))
                .utmContent(
                        bundle.getString(ARG_UTM_CONTENT, VersionInfo.getVersionInfo(getActivity()))
                )
                .idemPotencyKey(generateATokenRechargeCheckout())
                .utmSource(bundle.getString(ARG_UTM_SOURCE, "android"))
                .utmMedium(bundle.getString(ARG_UTM_MEDIUM, "widget"))
                .build();
    }

    private String generateATokenRechargeCheckout() {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = AuthUtil.md5(timeMillis);
        return SessionHandler.getLoginID(getActivity()) + "_"
                + (token.isEmpty() ? timeMillis : token);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            UnifyTracking.eventSelectOperatorWidget(category.getAttributes().getName(),
                    selectedOperator == null ? "" : selectedOperator.name);
            setParentToScroolToTop();
        }
    }

    private void setParentToScroolToTop() {
        if (getParentFragment() != null && getParentFragment() instanceof FragmentIndexCategory) {
            ((FragmentIndexCategory) getParentFragment()).scrollUntilBottomBanner();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG, "onTouch() called with: " + "v = [" + v + "], event = [" + event + "]");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                rechargeEditText.getAutoCompleteTextView().setFocusable(true);
                rechargeEditText.getAutoCompleteTextView().requestFocus();
                break;
            case MotionEvent.ACTION_UP:
                setParentToScroolToTop();
                break;
            default:
                break;
        }
        return false;
    }


    private class Contact {
        String givenName;
        String contactNumber;
        int contactType;
    }

    private void sendGTMClickBeli() {
        String labelBeli;
        switch (category.getId()) {
            case 1:
                labelBeli = AppEventTracking.EventLabel.PULSA_WIDGET;
                break;
            case 2:
                labelBeli = AppEventTracking.EventLabel.PAKET_DATA_WIDGET;
                break;
            case 3:
                labelBeli = AppEventTracking.EventLabel.PLN_WIDGET;
                break;
            default:
                labelBeli = AppEventTracking.EventLabel.PULSA_BELI;
        }
        UnifyTracking.eventRechargeBuy(labelBeli);
    }

    private void storeLastStateTabSelected() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(
                getActivity(), TkpdCache.CACHE_RECHARGE_WIDGET_TAB_SELECTION
        );
        localCacheHandler.putInt(TkpdCache.Key.WIDGET_RECHARGE_TAB_LAST_SELECTED, currentPosition);
        localCacheHandler.applyEditor();
    }

    //endregion

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RechargeFragmentPermissionsDispatcher.onRequestPermissionsResult(
                RechargeFragment.this, requestCode, grantResults);
    }

    @OnPermissionDenied(Manifest.permission.READ_CONTACTS)
    void showDeniedForContacts() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_CONTACTS);

    }

    @OnNeverAskAgain(Manifest.permission.READ_CONTACTS)
    void showNeverAskForContacts() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_CONTACTS);

    }

    @OnShowRationale(Manifest.permission.READ_CONTACTS)
    void showRationaleForContacts(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(getActivity(), request, Manifest.permission.READ_CONTACTS);
    }

    public void setupArguments(Bundle argument) {
        bundle = argument;
        category = bundle.getParcelable(ARG_PARAM_CATEGORY);
        rechargePresenter = new RechargePresenterImpl(getContext(), this);
    }

    private void storeLastClientNumberTyped(String categoryId, String clientNumber) {
        if (localCacheHandlerLastClientNumber == null)
            localCacheHandlerLastClientNumber = new LocalCacheHandler(
                    getActivity(), TkpdCache.DIGITAL_LAST_INPUT_CLIENT_NUMBER
            );
        localCacheHandlerLastClientNumber.putString(
                TkpdCache.Key.DIGITAL_CLIENT_NUMBER_CATEGORY + categoryId, clientNumber
        );
        localCacheHandlerLastClientNumber.putString(
                TkpdCache.Key.DIGITAL_OPERATOR_ID_CATEGORY + categoryId,
                (selectedProduct != null ?
                        String.valueOf(
                                selectedProduct.getRelationships().getOperator().getData().getId()
                        ) : "")
        );
        localCacheHandlerLastClientNumber.putString(
                TkpdCache.Key.DIGITAL_PRODUCT_ID_CATEGORY + categoryId,
                (selectedProduct != null ? String.valueOf(selectedProduct.getId()) : "")
        );
        localCacheHandlerLastClientNumber.applyEditor();
    }

    private String getLastClientNumberTyped(String categoryId) {
        if (localCacheHandlerLastClientNumber == null)
            localCacheHandlerLastClientNumber = new LocalCacheHandler(
                    getActivity(), TkpdCache.DIGITAL_LAST_INPUT_CLIENT_NUMBER);
        return localCacheHandlerLastClientNumber.getString(
                TkpdCache.Key.DIGITAL_CLIENT_NUMBER_CATEGORY + categoryId, ""
        );
    }

    private String getLastOperatorSelected(String categoryId) {
        if (localCacheHandlerLastClientNumber == null)
            localCacheHandlerLastClientNumber = new LocalCacheHandler(
                    getActivity(), TkpdCache.DIGITAL_LAST_INPUT_CLIENT_NUMBER);
        return localCacheHandlerLastClientNumber.getString(
                TkpdCache.Key.DIGITAL_OPERATOR_ID_CATEGORY + categoryId, ""
        );
    }

    private String getLastProductSelected(String categoryId) {
        if (localCacheHandlerLastClientNumber == null)
            localCacheHandlerLastClientNumber = new LocalCacheHandler(
                    getActivity(), TkpdCache.DIGITAL_LAST_INPUT_CLIENT_NUMBER);
        return localCacheHandlerLastClientNumber.getString(
                TkpdCache.Key.DIGITAL_PRODUCT_ID_CATEGORY + categoryId, ""
        );
    }
}