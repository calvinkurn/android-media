package com.tokopedia.core.invoice.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
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
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.invoice.listener.InvoiceViewListener;
import com.tokopedia.core.invoice.model.InvoiceRenderParam;
import com.tokopedia.core.invoice.presenter.InvoiceRenderPresenter;
import com.tokopedia.core.invoice.presenter.InvoiceRenderPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.TkpdWebView;

import butterknife.BindView;

/**
 * InvoiceRendererActivity
 * Created by Angga.Prasetiyo on 15/06/2016.
 */
public class InvoiceRendererActivity extends BasePresenterActivity<InvoiceRenderPresenter>
        implements InvoiceViewListener {
    private static final String TAG = InvoiceRendererActivity.class.getSimpleName();

    private static final String EXTRA_INVOICE_RENDER_PARAM = "EXTRA_INVOICE_RENDER_PARAM";

    @BindView(R2.id.webview)
    TkpdWebView webViewOauth;
    @BindView(R2.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R2.id.layout_parent)
    FrameLayout layoutParent;

    private InvoiceRenderParam invoiceParam;
    private TkpdProgressDialog progressDialog;

    public static Intent newInstance(Context context, InvoiceRenderParam data) {
        Intent intent = new Intent(context, InvoiceRendererActivity.class);
        intent.putExtra(EXTRA_INVOICE_RENDER_PARAM, data);
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
        this.invoiceParam = extras.getParcelable(EXTRA_INVOICE_RENDER_PARAM);
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
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            MenuItem menuItem = menu.findItem(R.id.action_print);
            menuItem.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
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
        } else {
            // Show dialog
        }
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
        PrintJob printJob = printManager.print(jobName, printAdapter, builder.build());
        if (printJob.isCompleted()) {
            Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_LONG).show();
        } else if (printJob.isFailed()) {
            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_print) {
            onMenuPrintClcked();
            return true;
        } else if(item.getItemId() == android.R.id.home){
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
