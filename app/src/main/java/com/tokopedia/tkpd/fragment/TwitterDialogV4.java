package com.tokopedia.tkpd.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.UrlQuerySanitizer;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.tkpd.R;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by noiz354 on 5/3/16.
 */
public class TwitterDialogV4 extends DialogFragment{
    private WebView webViewOauth;
    private Activity activity;

    static String TWITTER_CONSUMER_KEY = "52KGKAetD2PxYTpAZxrg";
    static String TWITTER_CONSUMER_SECRET = "r12evmMVyGTt3uwrZUXNSS0sL32Uxw4ghZ0RkBIRlk4";

    static final String TWITTER_CALLBACK_URL = "oauth://tokopedia";

    private static Twitter twitter;
    private static RequestToken requestToken;
    private String oauth_token;
    private String oauth_verifier;
    private Context context;
    private TwitterInterface TwitterInt;
    private ProgressBar loadingBar;

    public interface TwitterInterface {
        public void ChangeUI();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        context = getActivity();
        this.activity = activity;
        TwitterInt = (TwitterInterface)context;
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url)  {
            webViewOauth.setVisibility(View.VISIBLE);
            CommonUtils.dumper("FINISH LOADING!");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //check if the login was successful and the access token returned
            //this test depend of your API
            System.out.println(url);
            if (url.contains("oauth://tokopedia?oauth_token")) {
                //save your token
                saveAccessToken(url);
                return true;
            }else if (url.contains("oauth://tokopedia?denied")) {
                DismissDialog();
                return false;
            }
            // BaseActivity.logEvent(Consts.EVENT_CALLBACK + "Login Failed", true);
            return false;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
        }
    }

    private void DismissDialog() {

    }

    private void saveAccessToken(String url) {
        // extract the token if it exists

        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer();
        sanitizer.setAllowUnregisteredParamaters(true);
        sanitizer.parseUrl(url);
        oauth_token = sanitizer.getValue("oauth_token");
        oauth_verifier = sanitizer.getValue("oauth_verifier");
        System.out.println(oauth_token);
        new getOauthData ().execute();


        //  Intent intent2 = new Intent(getActivity(), WallScreenActivity.class);
        //intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //startActivity(intent2);
        return;

    }

    @Override
    public void onViewCreated(View arg0, Bundle arg1) {
        super.onViewCreated(arg0, arg1);
        new LoginTwitter().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Retrieve the webview
        View v = inflater.inflate(R.layout.fragment_webview_oath, container, false);
        webViewOauth = (WebView) v.findViewById(R.id.web_oauth);
        loadingBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        return v;
    }

    class LoginTwitter extends AsyncTask<Void, Boolean, Boolean> {

        /**
         * Before starting background thread Show Progress Dialog
         * */

        /**
         * getting Places JSON
         * */
        protected Boolean doInBackground(Void... args) {
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
                Configuration configuration = builder.build();

                TwitterFactory factory = new TwitterFactory(configuration);
                twitter = factory.getInstance();
                requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
                return true;

            } catch (TwitterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }

        /**
         * After completing background task Dismiss the progress dialog and show
         * the data in UI Always use runOnUiThread(new Runnable()) to update UI
         * from background thread, otherwise you will get error
         * **/
        protected void onPostExecute(Boolean Result) {
            if(Result){
                //load the url of the oAuth login page
                webViewOauth.loadUrl(requestToken.getAuthenticationURL());
                //set the web client
                webViewOauth.setWebChromeClient(new MyWebChrome());
                webViewOauth.setWebViewClient(new MyWebViewClient());
                //activates JavaScript (just in case)
                WebSettings webSettings = webViewOauth.getSettings();
                webSettings.setJavaScriptEnabled(true);
            }
        }
    }

    private class MyWebChrome extends WebChromeClient {
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            ConsoleMessage.MessageLevel level = consoleMessage.messageLevel();
            //if (level.ordinal() == 3) { // Error ordinal
            //	webViewOauth.stopLoading();
            //	webViewOauth.loadUrl(url);
            //}
            //CommonUtils.dumper(consoleMessage.message());
            //CommonUtils.UniversalToast(context, consoleMessage.message());

            return false;
        }

        @Override
        public void onProgressChanged(WebView view, int progress) {
            // Activities and WebViews measure progress with different scales.
            // The progress meter will automatically disappear when we reach
            // 100%
            activity.setProgress(progress * 1000);
            loadingBar.setProgress(progress);
        }

    }

    class getOauthData extends AsyncTask<Void, Boolean, Boolean> {

        /**
         * Before starting background thread Show Progress Dialog
         * */

        /**
         * getting Places JSON
         * */
        @Override
        protected Boolean doInBackground(Void... args) {
            AccessToken accessToken;
            try {
                System.out.println(oauth_verifier);
                accessToken = twitter.getOAuthAccessToken(requestToken, oauth_verifier);
                final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("TWITTER_OAUTH", 0);
                final SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("TWITTER_OAUTH_TOKEN", accessToken.getToken());
                edit.putString("TWITTER_OAUTH_SCREET", accessToken.getTokenSecret());
                edit.putString("TWITTER_OAUTH_VERIFIER", oauth_verifier);
                edit.putBoolean("TWITTER_IS_LOGIN", true);
                edit.commit();
                System.out.println("screet: "+accessToken.getTokenSecret());
            } catch (TwitterException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
			/*ImageView iv = (ImageView) getView().findViewById(R.shopId.twitter);
			iv.setImageResource(R.drawable.twitter_square_blue);*/
            return true;
        }

        /**
         * After completing background task Dismiss the progress dialog and show
         * the data in UI Always use runOnUiThread(new Runnable()) to update UI
         * from background thread, otherwise you will get error
         * **/
        @Override
        protected void onPostExecute(Boolean status) {
            TwitterInt.ChangeUI();
            dismiss();
        }

    }
}
