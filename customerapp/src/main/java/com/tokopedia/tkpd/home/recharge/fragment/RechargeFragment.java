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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
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
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.VersionInfo;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;
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
    //endregion

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
                    if (intent.hasExtra(IDigitalModuleRouter.EXTRA_MESSAGE)) {
                        String message = intent.getStringExtra(IDigitalModuleRouter.EXTRA_MESSAGE);
                        if (!TextUtils.isEmpty(message)) {
                            NetworkErrorHelper.showSnackbar(getActivity(), message);
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
                String phonePrefix = temp.substring(0, temp.length() <= 4 ? temp.length() : 4);
                if (s.length() >= 3) {
                    this.rechargePresenter.validatePhonePrefix(phonePrefix,
                            category.getId(),
                            category.getAttributes().getValidatePrefix());
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
        LocalCacheHandler.clearCache(getActivity(), KEY_PHONEBOOK);
        rechargePresenter.clearRechargePhonebookCache();
        hideFormAndImageOperator();
    }

    private boolean isValidatePrefix() {
        return category.getAttributes().getValidatePrefix();
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
    }

    @Override
    public void onButtonContactClicked() {
        RechargeFragmentPermissionsDispatcher.doLaunchContactPickerWithCheck(RechargeFragment.this);
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
            gotoLogin();
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
                !category.getAttributes().getClientNumber().getIsShown()) {
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
        return rechargeEditText.getText().length() >= 0;
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
            spnOperator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    rechargeEditText.setEmptyString();
                    selectedOperator = operatorList.get(i);
                    selectedOperatorId = Integer.toString(selectedOperator.operatorId);
                    if (!category.getAttributes().getClientNumber().getIsShown()) {
                        setUpForNotUsingTextEdit();
                    } else {
                        rechargePresenter.updateMinLenghAndOperator(selectedOperatorId);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    private void setSpnNominalSelectionBasedLastOrder(List<Product> productList) {
        if (lastOrder != null && lastOrder.getData() != null
                && lastOrder.getData().getAttributes() != null) {
            int lastProductId = lastOrder.getData().getAttributes().getProduct_id();
            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i).getId().equals(lastProductId)) {
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
        if (checkStockProduct(selectedProduct))
            goToNativeCheckout();
//            goToCheckout(getUrlCheckout());
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
        buyWithCreditCheckbox.setOnCheckedChangeListener(this);
    }

    private void renderDefaultView(CategoryAttributes categoryAttributes) {
        ClientNumber clientNumber = categoryAttributes.getClientNumber();
        tlpLabelTextView.setText(clientNumber.getText());
        rechargeEditText.setHint(clientNumber.getPlaceholder());
        buyWithCreditCheckbox.setVisibility(
                categoryAttributes.getInstantCheckoutAvailable() ? View.VISIBLE : View.GONE
        );

        setTextToEditTextOrSetVisibilityForm();
        setPhoneBookVisibility();

        if (!category.getAttributes().getValidatePrefix()) {
            selectedOperatorId = category.getAttributes().getDefaultOperatorId();
            if (!category.getAttributes().getClientNumber().getIsShown()) {
                this.rechargePresenter.updateMinLenghAndOperator(selectedOperatorId);
                setUpForNotUsingTextEdit();
            }

            if (category.getAttributes().getShowOperator()) {
                this.rechargePresenter.getListOperatorFromCategory(category.getId());
            }
        }
    }

    private void setUpForNotUsingTextEdit() {
        tlpLabelTextView.setVisibility(View.GONE);
        rechargeEditText.setVisibility(View.GONE);
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
        checkRadioButtonBasedOnLastOrder(operators, radGroup);
        selectedOperatorId = Integer.toString(selectedOperator.operatorId);
        minLengthDefaultOperator = selectedOperator.minimumLength;
        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                hideFormAndImageOperator();
                rechargeEditText.setEmptyString();
                selectedProduct = null;
                selectedOperator = operators.get(i);
                selectedOperatorId = Integer.toString(operators.get(i).operatorId);
                minLengthDefaultOperator = selectedOperator.minimumLength;
                rechargePresenter.updateMinLenghAndOperator(selectedOperatorId);
            }
        });
    }

    private void setTextToEditTextOrSetVisibilityForm() {
        cacheHandlerPhoneBook = new LocalCacheHandler(getActivity(), KEY_PHONEBOOK);
        if (rechargePresenter.isAlreadyHavePhonebookDataOnCache(LAST_INPUT_KEY + category.getId())) {
            rechargeEditText.setText(
                    rechargePresenter.getLastInputFromCache(LAST_INPUT_KEY + category.getId())
            );
            showFormAndImageOperator();
        } else if (!TextUtils.isEmpty(
                cacheHandlerPhoneBook.getString(KEY_PHONEBOOK + category.getId()))
                ) {
            rechargeEditText.setText(
                    cacheHandlerPhoneBook.getString(KEY_PHONEBOOK + category.getId())
            );
            LocalCacheHandler.clearCache(getActivity(), KEY_PHONEBOOK);
            showFormAndImageOperator();
        } else if (SessionHandler.isV4Login(getActivity())
                && rechargePresenter.isAlreadyHaveLastOrderDataOnCache()) {
            renderLastOrder();
        } else {
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
        if (rechargeEditText != null && !rechargeEditText.getText().toString().trim().equals("")) {
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
            if (categoryAttributes.getUsePhonebook() && rechargeEditText != null) {

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
        Intent intent = SessionRouter.getLoginActivityIntent(getActivity());
        intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
        rechargePresenter.saveLastInputToCache(
                LAST_INPUT_KEY + category.getId(),
                rechargeEditText.getText()
        );

        startActivityForResult(intent, LOGIN_REQUEST_CODE);


    }

    private String getUrlCheckout() {
        String clientNumber = rechargeEditText.getText();
        String url = TkpdBaseURL.PULSA_WEB_DOMAIN + "?" +
                "action=init_data" +
                "&client_number=" + clientNumber +
                "&product_id=" + selectedProduct.getId().toString() +
                "&operator_id=" + selectedProduct.getRelationships().getOperator().getData().getId().toString() +
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
        String clientNumber = rechargeEditText.getText();
        DigitalCheckoutPassData digitalCheckoutPassData = new DigitalCheckoutPassData.Builder()
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
        if (getActivity().getApplication() instanceof IDigitalModuleRouter) {
            IDigitalModuleRouter digitalModuleRouter = (IDigitalModuleRouter) getActivity().getApplication();
            startActivityForResult(
                    digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassData),
                    IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL
            );
        }
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
        CommonUtils.dumper("GAv4 category clicked " + category.getId());
        CommonUtils.dumper("GAv4 clicked beli Pulsa");
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
}