package com.tokopedia.tkpd.rescenter.create.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.BasePresenterFragment;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.rescenter.create.customview.ChooseCategorySectionCreateResCenterView;
import com.tokopedia.tkpd.rescenter.create.customview.ChooseProductSectionCreateResCenterView;
import com.tokopedia.tkpd.rescenter.create.customview.ChooseTroubleSectionCreateResCenterView;
import com.tokopedia.tkpd.rescenter.create.listener.ChooseTroubleListener;
import com.tokopedia.tkpd.rescenter.create.listener.CreateResCenterListener;
import com.tokopedia.tkpd.rescenter.create.model.passdata.ActionParameterPassData;
import com.tokopedia.tkpd.rescenter.create.model.responsedata.CreateResCenterFormData;
import com.tokopedia.tkpd.rescenter.create.presenter.ChooseTroubleImpl;
import com.tokopedia.tkpd.rescenter.create.presenter.ChooseTroublePresenter;
import com.tokopedia.tkpd.util.AppUtils;

import butterknife.Bind;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChooseTroubleFragment extends BasePresenterFragment<ChooseTroublePresenter>
        implements ChooseTroubleListener {

    private static final String KEY_PARAM_PASS_DATA = "pass_data";
    private CreateResCenterListener listener;

    @Bind(R.id.invoice)
    TextView invoice;
    @Bind(R.id.shop_name)
    TextView shopName;
    @Bind(R.id.view_category_section)
    ChooseCategorySectionCreateResCenterView categorySection;
    @Bind(R.id.view_trouble_section)
    ChooseTroubleSectionCreateResCenterView troubleSectionView;
    @Bind(R.id.view_product_section)
    ChooseProductSectionCreateResCenterView productSection;
    @Bind(R.id.action_choose_solution)
    View chooseSolution;
    @Bind(R.id.action_abort)
    View actionAbort;
    @Bind(R.id.main_view)
    View mainView;
    @Bind(R.id.include_loading)
    View loading;

    private ActionParameterPassData passData;

    public static Fragment newInstance(ActionParameterPassData passData) {
        ChooseTroubleFragment fragment = new ChooseTroubleFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_PARAM_PASS_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ChooseTroubleFragment() {
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.setOnFirstTimeLaunched(getActivity(), passData);
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putParcelable(KEY_PARAM_PASS_DATA, passData);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        passData = savedState.getParcelable(KEY_PARAM_PASS_DATA);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ChooseTroubleImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {
        listener = (CreateResCenterListener) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        passData = arguments.getParcelable(KEY_PARAM_PASS_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_choose_trouble;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {
        categorySection.setListener(this);
        troubleSectionView.setListener(this);
        productSection.setListener(this);
        chooseSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardHandler.DropKeyboard(getActivity(), getView());
                presenter.setOnChooseSolutionClick(getActivity());
            }
        });
        actionAbort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(R.string.dialog_discard_changes)
                        .setTitle(R.string.dialog_title_discard_changes)
                        .setPositiveButton(R.string.action_discard, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton(R.string.action_keep, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                Dialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void openSolutionFragment() {
        listener.addSolutionFragmentStacked(passData);
    }

    @Override
    public void openProductDetailTroubleFragment() {
        listener.addProductDetailTroubleFragmentStacked(passData);
    }

    @Override
    public void showLoading(boolean isShow) {
        if (isShow) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMainView(boolean isShow) {
        if (isShow) {
            mainView.setVisibility(View.VISIBLE);
        } else {
            mainView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setErrorFullView(String error, NetworkErrorHelper.RetryClickedListener clickedListener) {
        if (error != null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, clickedListener);
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), clickedListener);
        }
    }

    @Override
    public void setTimeOutFullView(NetworkErrorHelper.RetryClickedListener clickedListener) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), clickedListener);
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void renderData(CreateResCenterFormData data) {
        passData.setFormData(data);
        categorySection.renderData(data);
        productSection.renderData(data);
        troubleSectionView.renderData(data);
        renderInvoiceData(data.getForm());
        renderShopData(data.getForm());
    }

    private void renderShopData(CreateResCenterFormData.FormValueData form) {
        SpannableString spannableString = new SpannableString(getString(R.string.title_purchase_from).replace("XYZ", form.getOrderShopName()));
        String mShopName = form.getOrderShopName();

        spannableString.setSpan(
                new ClickableSpan() {
                    @Override
                    public void onClick(View view) {

                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setUnderlineText(false);
                        ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        ds.setColor(ContextCompat.getColor(getActivity(), R.color.href_link));
                    }
                },
                spannableString.toString().indexOf(mShopName),
                spannableString.toString().indexOf(mShopName) + mShopName.length(),
                0
        );

        shopName.setMovementMethod(LinkMovementMethod.getInstance());
        shopName.setText(spannableString);
    }

    private void renderInvoiceData(final CreateResCenterFormData.FormValueData form) {
        SpannableString spannableString = new SpannableString(form.getOrderInvoiceRefNum());
        String invoiceRefNum = form.getOrderInvoiceRefNum();

        spannableString.setSpan(
                new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        AppUtils.InvoiceDialog(
                                getActivity(),
                                form.getOrderPdfUrl(),
                                form.getOrderInvoiceRefNum()
                        );
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setUnderlineText(false);
                        ds.setColor(ContextCompat.getColor(getActivity(), R.color.href_link));
                    }
                },
                spannableString.toString().indexOf(invoiceRefNum),
                spannableString.toString().indexOf(invoiceRefNum) + invoiceRefNum.length(),
                0
        );

        invoice.setMovementMethod(LinkMovementMethod.getInstance());
        invoice.setText(spannableString);
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public ActionParameterPassData collectInputData() {
        passData.setTroubleCategoryChoosen(categorySection.getTroubleCategoryChoosen());
        passData.setProductTroubleChoosenList(productSection.getProductTrouble());
        passData.setTroubleChoosen(troubleSectionView.getTroubleChoosen());
        passData.setInputDescription(troubleSectionView.getDescription());
        return passData;
    }

    @Override
    public void showChooseTrouble(boolean isVisible) {
        if (isVisible) {
            troubleSectionView.setVisibility(View.VISIBLE);
            troubleSectionView.renderSpinner(categorySection.getTroubleCategoryChoosen());
        } else {
            troubleSectionView.setVisibility(View.GONE);
            troubleSectionView.resetSpinner();
        }
    }

    @Override
    public void showChooseProduct(boolean isVisible) {
        productSection.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroy() {
        presenter.onsubscribe();
        super.onDestroy();
    }
}
