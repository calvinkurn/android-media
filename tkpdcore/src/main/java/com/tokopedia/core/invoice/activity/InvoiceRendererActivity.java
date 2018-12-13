package com.tokopedia.core.invoice.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.customView.TextDrawable;
import com.tokopedia.core.invoice.listener.InvoiceViewListener;
import com.tokopedia.core.invoice.model.InvoiceRenderParam;
import com.tokopedia.core.invoice.presenter.InvoiceRenderPresenter;
import com.tokopedia.core.invoice.presenter.InvoiceRenderPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.TkpdWebView;
import com.tokopedia.design.component.Dialog;

import butterknife.BindView;

/**
 * InvoiceRendererActivity
 * Created by Angga.Prasetiyo on 15/06/2016.
 */
public class InvoiceRendererActivity extends BasePresenterActivity<InvoiceRenderPresenter>
        implements InvoiceViewListener {
    private static final String TAG = InvoiceRendererActivity.class.getSimpleName();

    private static final String TOP_SELLER_APPLICATION_PACKAGE = "com.tokopedia.sellerapp";

    private static final String EXTRA_INVOICE_RENDER_PARAM = "EXTRA_INVOICE_RENDER_PARAM";
    private static final String EXTRA_INVOICE_SELLER = "EXTRA_INVOICE_SELLER";

    @BindView(R2.id.webview)
    TkpdWebView webViewOauth;
    @BindView(R2.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R2.id.layout_parent)
    FrameLayout layoutParent;

    private InvoiceRenderParam invoiceParam;
    private TkpdProgressDialog progressDialog;
    private boolean seller;

    public static Intent newInstance(Context context, InvoiceRenderParam data, boolean seller) {
        Intent intent = new Intent(context, InvoiceRendererActivity.class);
        intent.putExtra(EXTRA_INVOICE_RENDER_PARAM, data);
        intent.putExtra(EXTRA_INVOICE_SELLER, seller);
        return intent;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_DOWNLOAD_INVOICE;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        invoiceParam = extras.getParcelable(EXTRA_INVOICE_RENDER_PARAM);
        seller = extras.getBoolean(EXTRA_INVOICE_SELLER);
    }

    @Override
    protected void initialPresenter() {
        presenter = new InvoiceRenderPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview_general;
    }

    @Override
    protected void initView() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void setViewListener() {
        if (progressBar != null) progressBar.setIndeterminate(true);
        webViewOauth.getSettings().setJavaScriptEnabled(true);
        webViewOauth.getSettings().setBuiltInZoomControls(false);
        webViewOauth.getSettings().setDisplayZoomControls(true);
        webViewOauth.setWebChromeClient(new MyWebChrome());
        webViewOauth.setWebViewClient(new MyWebViewClient());
        webViewOauth.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                return false;
            }
        });
    }

    @Override
    protected void initVar() {
        progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        progressDialog.setCancelable(false);
    }

    @Override
    protected void setActionVar() {
        presenter.processGetRenderedInvoice(this, invoiceParam);
    }

    @Override
    public void renderInvoiceWeb(String htmlSource) {
        webViewOauth.loadData(htmlSource, "text/html", "UTF-8");
    }

    @Override
    public void showProgress() {
        progressDialog.showDialog();
    }

    @Override
    public void dismissProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void renderInvoiceError(String message) {
        if (progressBar != null && progressBar.getVisibility() == View.VISIBLE)
            progressBar.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(this, layoutParent, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.processGetRenderedInvoice(InvoiceRendererActivity.this, invoiceParam);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_transaction_invoice, menu);
        if (seller && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MenuItem menuItem = menu.findItem(R.id.action_print);
            menuItem.setVisible(true);
            menuItem.setIcon(getMenuPrintDrawableText());
        }
        return super.onCreateOptionsMenu(menu);
    }

    private Drawable getMenuPrintDrawableText() {
        TextDrawable drawable = new TextDrawable(this);
        drawable.setText(getResources().getString(R.string.action_print));
        drawable.setTextColor(ContextCompat.getColor(this, R.color.tkpd_main_green));
        return drawable;
    }

    private class MyWebChrome extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (progressBar != null) progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            if (progressBar != null) progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(final WebView view, String url, Bitmap favicon) {
            if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void onMenuPrintClcked() {
        if (GlobalConfig.isSellerApp()) {
            createWebPagePrint(webViewOauth);
        } else if (seller) {
            showDownloadSellerAppDialog();
        }
    }

    private void showDownloadSellerAppDialog() {
        final Dialog dialog = new Dialog(this, Dialog.Type.LONG_PROMINANCE);
        dialog.setTitle(getString(R.string.invoice_print_dialog_title));
        dialog.setDesc(getString(R.string.invoice_print_dialog_description));
        dialog.setBtnOk(getString(R.string.invoice_print_dialog_button_ok));
        dialog.setBtnCancel(getString(R.string.invoice_print_dialog_button_cancel));
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFinishing()) {
                    return;
                }
                goToSellerApp();
                dialog.dismiss();
            }

            private void goToSellerApp() {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(TOP_SELLER_APPLICATION_PACKAGE);
                if (launchIntent != null) {
                    startActivity(launchIntent);
                } else if (getApplication() instanceof TkpdCoreRouter) {
                    ((TkpdCoreRouter) getApplication()).goToCreateMerchantRedirect(InvoiceRendererActivity.this);
                }
            }
        });
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFinishing()) {
                    return;
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void createWebPagePrint(WebView webView) {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        String jobName = getString(R.string.app_name) + " Document";
        PrintDocumentAdapter printAdapter;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            printAdapter = webView.createPrintDocumentAdapter(jobName);
        } else {
            printAdapter = webView.createPrintDocumentAdapter();
        }
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A5);
        if (printManager != null) {
            printManager.print(jobName, printAdapter, builder.build());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_print) {
            onMenuPrintClcked();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
