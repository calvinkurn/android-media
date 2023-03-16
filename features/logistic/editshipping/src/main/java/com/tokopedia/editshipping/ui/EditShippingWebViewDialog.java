package com.tokopedia.editshipping.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tokopedia.abstraction.base.view.webview.TkpdWebView;
import com.tokopedia.editshipping.R;
import com.tokopedia.editshipping.databinding.EditShippingWebViewBinding;
import com.tokopedia.unifyprinciples.Typography;

/**
 * Created by Kris on 5/12/2016.
 * TOKOPEDIA
 */
public class EditShippingWebViewDialog extends DialogFragment {

    private class AdditionalOptionsWebViewClient extends WebViewClient {
        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (editButtonClicked) {
                Intent intent = new Intent();
                String resultShippingResultKey;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    resultShippingResultKey = replaceTagHtml(url);
                } else {
                    resultShippingResultKey = url.replaceAll(".*</html>", "");
                }
                intent.putExtra(EditShippingViewListener.EDIT_SHIPPING_RESULT_KEY, resultShippingResultKey);
                intent.putExtra(EditShippingViewListener.MODIFIED_COURIER_INDEX_KEY, courierIndex);
                getTargetFragment().onActivityResult(EditShippingViewListener
                        .ADDITIONAL_OPTION_REQUEST_CODE, Activity.RESULT_OK, intent);
                dismiss();
            }
        }

        private String replaceTagHtml(String url) {
            String htmlTag = "</html>";
            int indexTagHtml = url.indexOf(htmlTag);
            return url.substring(indexTagHtml + htmlTag.length());
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }


    }
    private boolean editButtonClicked = false;
    private int courierIndex;
    private static final String WEB_RESOURCE_KEY = "web_resource_key";
    private static final String COURIER_INDEX_KEY = "courier_index_key";

    public static EditShippingWebViewDialog openAdditionalOptionDialog(String webResource, int courierIndex) {
        EditShippingWebViewDialog dialog = new EditShippingWebViewDialog();
        Bundle bundle = new Bundle();
        bundle.putString(WEB_RESOURCE_KEY, webResource);
        bundle.putInt(COURIER_INDEX_KEY, courierIndex);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EditShippingWebViewBinding view = EditShippingWebViewBinding.inflate(inflater, container, false);
        courierIndex = getArguments().getInt(COURIER_INDEX_KEY);
        view.additionalOptionDialog.setWebViewClient(new AdditionalOptionsWebViewClient());
        view.additionalOptionDialog.setWebChromeClient(new WebChromeClient());
        view.additionalOptionDialog.loadData(getArguments().getString(WEB_RESOURCE_KEY), "text/html", "UTF-8");
        view.additionalOptionDialog.getSettings().setJavaScriptEnabled(true);
        view.editOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editButtonClicked = true;
                view.additionalOptionDialog.loadUrl("javascript:SubmitNewWebview();");
            }
        });
        //webView.loadUrl("http://new.ph-peter.ndvl/web-service/v4/web-view/get_shipping_detail_info.pl?shipping_id=1&user_id=2828&os_type=2&service_id=1");
        view.closeOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

}
