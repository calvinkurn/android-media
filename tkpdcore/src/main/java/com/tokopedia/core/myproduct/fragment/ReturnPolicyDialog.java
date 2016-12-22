package com.tokopedia.core.myproduct.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.DownloadResultSender;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.myproduct.model.CreateShopNoteParam;
import com.tokopedia.core.myproduct.model.EditShopNoteParam;
import com.tokopedia.core.myproduct.model.NoteDetailModel;
import com.tokopedia.core.myproduct.service.ProductService;
import com.tokopedia.core.network.apiservices.shop.MyShopNoteActService;
import com.tokopedia.core.network.retrofit.services.AuthService;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by m.normansyah on 17/12/2015.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class ReturnPolicyDialog extends DialogFragment {

    public static final int RETURN_POLICY_TYPE = 1;
    private static final String TAG = ReturnPolicyDialog.class.getSimpleName();
    private static final String messageTAG = TAG + " : ";
    public static final String FRAGMENT_TAG = TAG;
    private static final String NOTE_ID = "note_id";
    private static final String NOTE_TITLE = "NOTE_TITLE";
    private static final String NOTE_DETAIL = "NOTE_DETAIL";
    public static final String NOTE_CONTENT = "note_content";
    public static final String NOTE_TITLE1 = "note_title";
    public static final String TERMS = "terms";
    public static final String SIMPAN = "Simpan";
    String noteId;
    String noteTitle;
    NoteDetailModel.Detail data;

    AuthService service;

    @BindView(R2.id.return_policy_content)
    EditText returnPolicyContent;
    @BindView(R2.id.return_policy_cancel)
    Button returnPolicyCancel;
    @BindView(R2.id.return_policy_add)
    Button returnPolicyAdd;
    @BindView(R2.id.return_policy_progress_bar)
    ProgressBar returnPolicyProgressBar;
    @BindView(R2.id.return_policy_content_title)
    EditText returnPolicyContentTitle;
    private Unbinder unbinder;

    public static DialogFragment newInstance(NoteDetailModel.Detail detail) {
        Bundle arg = new Bundle();
        arg.putString(NOTE_ID, detail.getNotes_id());
        arg.putString(NOTE_TITLE, detail.getNotes_title());
        arg.putParcelable(NOTE_DETAIL, Parcels.wrap(detail));
        ReturnPolicyDialog returnPolicyDialog = new ReturnPolicyDialog();
        returnPolicyDialog.setArguments(arg);
        return returnPolicyDialog;
    }

    @Deprecated
    public static DialogFragment newInstance(String noteId, String noteTitle) {
        Bundle arg = new Bundle();
        arg.putString(NOTE_ID, noteId);
        arg.putString(NOTE_TITLE, noteTitle);
        ReturnPolicyDialog returnPolicyDialog = new ReturnPolicyDialog();
        returnPolicyDialog.setArguments(arg);
        return returnPolicyDialog;
    }

    public static DialogFragment newInstance() {
        return new ReturnPolicyDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Base_Theme_AppCompat_Dialog);
        service = new MyShopNoteActService();

        if (getArguments() != null) {
            if (getArguments().getString(NOTE_ID) != null) {
                noteId = getArguments().getString(NOTE_ID);
            }
            if (getArguments().getString(NOTE_TITLE) != null) {
                noteTitle = getArguments().getString(NOTE_TITLE);
            }
            if (getArguments().getParcelable(NOTE_DETAIL) != null) {
                data = Parcels.unwrap(getArguments().getParcelable(NOTE_DETAIL));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_return_policy_new, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (data != null) {
            returnPolicyContentTitle.setText(data.getNotes_title());
            returnPolicyContentTitle.setEnabled(false);
            if (CommonUtils.checkNullForZeroJson(data.getNotes_content())) {
                returnPolicyContent.setText(Html.fromHtml(data.getNotes_content()));
            }
            returnPolicyContent.requestFocus();
            returnPolicyAdd.setText(SIMPAN);
        } else {
            returnPolicyContentTitle.setText(getActivity().getString(R.string.title_returnable_policy));
            returnPolicyContentTitle.setEnabled(false);
            returnPolicyContent.requestFocus();
            returnPolicyAdd.setText(SIMPAN);
        }
    }

    public void showProgressBar() {
        //[START] display progressbar
        returnPolicyContent.setVisibility(View.GONE);
        returnPolicyContentTitle.setVisibility(View.GONE);
        returnPolicyProgressBar.setVisibility(View.VISIBLE);
        //[END] display progressbar
    }

    public void dismissProgressBar() {
        //[START] display progressbar
        returnPolicyContent.setVisibility(View.VISIBLE);
        returnPolicyContentTitle.setVisibility(View.VISIBLE);
        returnPolicyProgressBar.setVisibility(View.GONE);
        //[END] display progressbar
    }

    @OnClick(R2.id.return_policy_add)
    public void addReturnPolicy() {
        //[START] display progressbar
        showProgressBar();
        //[END] display progressbar

        if(returnPolicyContent.getText().toString().trim().isEmpty()){
            returnPolicyContent.setError(getString(R.string.empty_returnable_note));
            return;
        }

        if (data != null) {
            //[START] Old code just dismiss return policy
//            dismiss();
            //[END] Old code just dismiss return policy

            EditShopNoteParam editShopNoteParam = new EditShopNoteParam();
            editShopNoteParam.setNoteContent(returnPolicyContent.getText().toString());
            editShopNoteParam.setNoteId(data.getNotes_id());
            editShopNoteParam.setNoteTitle(returnPolicyContentTitle.getText().toString());
            Bundle bundle = new Bundle();
            bundle.putParcelable(ProductService.EDIT_SHOP_NOTE_PARAM, Parcels.wrap(editShopNoteParam));
            if (getActivity() != null && getActivity() instanceof DownloadResultSender) {
                ((DownloadResultSender) getActivity()).sendDataToInternet(ProductService.UPDATE_RETURNABLE_NOTE_ADD_PRODUCT, bundle);
            }

        } else if (!returnPolicyContent.getText().toString().isEmpty()) {

            CreateShopNoteParam createShopNoteParam = new CreateShopNoteParam();
            createShopNoteParam.setNoteContent(returnPolicyContent.getText().toString());
            createShopNoteParam.setNoteTitle(returnPolicyContentTitle.getText().toString());
            createShopNoteParam.setTerms(RETURN_POLICY_TYPE + "");

            Bundle bundle = new Bundle();
            bundle.putParcelable(ProductService.ADD_SHOP_NOTE_PARAM, Parcels.wrap(createShopNoteParam));
            if (getActivity() != null && getActivity() instanceof DownloadResultSender) {
                ((DownloadResultSender) getActivity()).sendDataToInternet(ProductService.ADD_RETURNABLE_NOTE_ADD_PRODUCT, bundle);
            }


        } else {
            CommonUtils.UniversalToast(getContext(), "Mohon Isi Kebijakan Pengembalian Produk");
        }


    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//    }

    @OnClick(R2.id.return_policy_cancel)
    public void dismissCancel() {
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static interface ReturnPolicyListener {
        void refreshReturnPolicy();

        void refreshReturnPolicy(String noteId);
    }
}
