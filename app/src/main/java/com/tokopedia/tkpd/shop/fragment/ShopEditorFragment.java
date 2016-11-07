package com.tokopedia.tkpd.shop.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.gallery.ImageGalleryEntry;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.prototype.ShopSettingCache;
import com.tokopedia.tkpd.session.base.BaseFragment;
import com.tokopedia.tkpd.shop.ShopEditService;
import com.tokopedia.tkpd.shop.model.shopData.Data;
import com.tokopedia.tkpd.shop.presenter.ShopEditorPresenter;
import com.tokopedia.tkpd.shop.presenter.ShopEditorPresenterImpl;
import com.tokopedia.tkpd.shop.presenter.ShopEditorView;
import com.tokopedia.tkpd.util.SessionHandler;
import com.tokopedia.tkpd.util.UploadImageReVamp;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Toped10 on 5/19/2016.
 */
public class ShopEditorFragment extends BaseFragment<ShopEditorPresenter> implements ShopEditorView {

    @Bind(R2.id.shop_name)
    EditText mShopNameText;
    @Bind(R2.id.shop_slogan)
    EditText mShopSloganText;
    @Bind(R2.id.shop_desc)
    EditText mShopDescText;
    @Bind(R2.id.button_send)
    TextView mBtnSend;
    @Bind(R2.id.progress_bar)
    ProgressBar progressBar;
    @Bind(R2.id.shop_ava)
    ImageView mShopAva;
    @Bind(R2.id.shop_editor_scrollview)
    ScrollView mShopEditor;
    @Bind(R2.id.edit_shop_schedule)
    ImageButton editShopSchedule;
    @Bind(R2.id.close_image)
    ImageView closeImage;
    @Bind(R2.id.shop_status)
    TextView shopStatus;
    @Bind(R2.id.schedule_info)
    TextView scheduleInfo;
    @Bind(R2.id.schedule_date)
    TextView scheduleDate;
    @Bind(R2.id.time_icon)
    ImageView timeIcon;
    @Bind(R2.id.icon_gold_merchant)
    ImageView icon_gold_merchant;
    @Bind(R2.id.status_gold)
    TextView status_gold;
    @Bind(R2.id.desc_status_gold)
    TextView desc_status;
    @Bind(R2.id.about_gm)
    TextView about_gm;
    private TkpdProgressDialog mProgressDialog;
    private UploadImageReVamp uploadimage2;

    @OnClick(R2.id.shop_ava)
    public void uploadImage(View vuew) {
        ImageGalleryEntry.moveToImageGallery((AppCompatActivity)getActivity(), 0, 1);
    }

    @OnClick(R2.id.button_send)
    public void kirimData(View view) {
        presenter.sendDataShop();
    }

    @Override
    public int getFragmentId() {
        return ShopEditorPresenter.FragmentId;
    }

    @Override
    public void ariseRetry(int type, Object... data) {

    }

    @Override
    public void setData(int type, Bundle data) {
    }

    @Override
    public void onNetworkError(int type, Object... data) {

    }

    @Override
    public void onMessageError(int type, Object... data) {
        CommonUtils.UniversalToast(getContext(), (String) (data[0]));
    }

    @Override
    protected void initPresenter() {
        presenter = new ShopEditorPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_editor;
    }

    @Override
    public void initView() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @OnClick(R2.id.edit_shop_schedule)
    void showEditShopScheduleDialog(){
        presenter.onClickCloseShop(presenter);
    }

    @OnClick(R2.id.about_gm)
    void showAboutGM(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gold.tokopedia.com/"));
        startActivity(browserIntent);
    }

    @Override
    public void initViewInstance() {
        mProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.MAIN_PROGRESS, getActivity().getWindow().getDecorView().getRootView());
        mProgressDialog.setLoadingViewId(R.id.include_loading);
    }

    @Override
    public View getRootView() {
        return getView();
    }

    @Override
    public void initAnalytics() {
//        AnalyticsHandler.init(getActivity()).
//                type(Type.GA).sendScreen(AppScreen.SCREEN_CONFIG_S_INFO);
    }

    @Override
    public void uploadImage(String data) {
        presenter.uploadUpdateImage(data);
    }

    @Override
    public void initUploadImage() {
        uploadimage2 = UploadImageReVamp.createInstance(getActivity(), "logo", UploadImageReVamp.WS_SHOP_EDITOR);
        uploadimage2.addParam("shop_id", SessionHandler.getShopID(getActivity()));
        uploadimage2.addParam("resolution", "215");
    }

    @Override
    public void setData(int type, Object... data) {
        switch (type) {
            case ShopEditorPresenter.SHOP_DESC_ERROR:
                mShopDescText.setError((String) data[0]);
                break;
            case ShopEditorPresenter.SHOP_NAME_ERROR:
                mShopNameText.setError((String) data[0]);
                break;
            case ShopEditorPresenter.SHOP_SLOGAN_ERROR:
                mShopSloganText.setError((String) data[0]);
                break;
            case ShopEditorPresenter.SHOP_NAME:
                mShopNameText.setText(Html.fromHtml((String) data[0]));
                break;
            case ShopEditorPresenter.SHOP_SLOGAN:
                mShopSloganText.setText(Html.fromHtml((String) data[0]));
                break;
            case ShopEditorPresenter.SHOP_DESC:
                mShopDescText.setText(Html.fromHtml((String) data[0]));
                break;
            default:
                throw new RuntimeException("please register type here!!!");
        }
    }

    @Override
    public Object getData(int type) {
        switch (type) {
            case ShopEditorPresenter.SHOP_NAME:
                return mShopNameText.getText().toString();
            case ShopEditorPresenter.SHOP_SLOGAN:
                return mShopSloganText.getText().toString();
            case ShopEditorPresenter.SHOP_DESC:
                return mShopDescText.getText().toString();
            default:
                return null;
        }
    }

    @Override
    public void loadImageAva(String mShopAvaUri, int ic_default_shop_ava) {
        ImageHandler.loadImage2(mShopAva, mShopAvaUri, ic_default_shop_ava);
    }

    @Override
    public void loadImageAva(String url) {
        ImageHandler.LoadImage(mShopAva, url);
    }

    @Override
    public void showButtonSend() {
        mBtnSend.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButttonSend() {
        mBtnSend.setVisibility(View.GONE);
    }

    @Override
    public void showDialog() {
        mProgressDialog.showDialog();
    }

    @Override
    public void showDialogNormal() {
        mProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        mProgressDialog.showDialog();
    }

    @Override
    public void hideDialog() {
        mProgressDialog.dismiss();
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAvaImage() {
        mShopAva.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAvaImage() {
        mShopAva.setVisibility(View.GONE);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideSoftInputWindow() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mShopNameText.getWindowToken(), 0);
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void showShopEditor() {
        mShopEditor.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideShopEditor() {
        mShopEditor.setVisibility(View.GONE);
    }

    @Override
    public void setOpenShop() {
        closeImage.setImageResource(R.drawable.icon_open);
        shopStatus.setText(getString(R.string.open));
        scheduleInfo.setText(getString(R.string.tidak_ada_jadwal));
        scheduleDate.setVisibility(View.GONE);
        timeIcon.setVisibility(View.GONE);
    }

    @Override
    public void setCloseShop(String closeEnd) {
        closeImage.setImageResource(R.drawable.icon_closed);
        shopStatus.setText(getString(R.string.close));
        scheduleInfo.setText("Sampai dengan " + closeEnd);
        scheduleDate.setVisibility(View.GONE);
        timeIcon.setVisibility(View.GONE);
    }

    @Override
    public void setCloseShopWithSchedule(String closeEnd) {
        closeImage.setImageResource(R.drawable.icon_open);
        shopStatus.setText(getString(R.string.open));
        scheduleInfo.setText(getString(R.string.jadwal_tutup_toko));
        scheduleDate.setText(closeEnd);
        scheduleDate.setVisibility(View.VISIBLE);
        timeIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void setShopIsGold(String until) {
        icon_gold_merchant.setVisibility(View.VISIBLE);
        status_gold.setText("Gold Merchant");
        desc_status.setText("Berlaku Sampai :" + until);
        about_gm.setText("Perpanjang Gold Merchant");
    }

    @Override
    public void setShopReguler() {
        icon_gold_merchant.setVisibility(View.GONE);
        status_gold.setText("Regular Merchant");
        desc_status.setText("Anda Belum Berlangganan Gold Merchant");
        about_gm.setText("Tentang Gold Merchant");
    }
}
