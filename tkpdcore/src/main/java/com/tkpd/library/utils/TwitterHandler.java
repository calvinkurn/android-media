package com.tkpd.library.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterHandler extends Activity {
    // Constants
    /**
     * Register your here app https://dev.twitter.com/apps/new and get your
     * consumer key and secret
     * */
    static String TWITTER_CONSUMER_KEY = "52KGKAetD2PxYTpAZxrg";
    static String TWITTER_CONSUMER_SECRET = "r12evmMVyGTt3uwrZUXNSS0sL32Uxw4ghZ0RkBIRlk4";

    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "TWITTER_OAUTH_TOKEN";
    static final String PREF_KEY_OAUTH_SECRET = "TWITTER_OAUTH_SCREET";
    static final String PREF_KEY_TWITTER_LOGIN = "TWITTER_IS_LOGIN";

    static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

    // Login button
    Button btnLoginTwitter;
    // Update status button
    Button btnUpdateStatus;
    // Logout button
    Button btnLogoutTwitter;
    // EditText for update
    EditText txtUpdate;
    // lbl update
    TextView lblUpdate;
    TextView lblUserName;

    // Progress dialog

    private Context context;

    // Twitter
    private static Twitter twitter;
    private static RequestToken requestToken;

    // Shared Preferences
    private static SharedPreferences mSharedPreferences;


    public TwitterHandler(Context context) {
        this.context = context;
        mSharedPreferences = context.getSharedPreferences("TWITTER_OAUTH", 0);

    }

    /**
     * Function to login twitter
     * */
    public void loginToTwitter() {
        // Check if already logged in
        new LoginTwitter().execute();
    }

    public void UpdateStatus(String status) {
        new updateTwitterStatus().execute(status);
    }

    /**
     * Function to update status
     * */
    class updateTwitterStatus extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress BaseDialog
         * */

        /**
         * getting Places JSON
         * */
        protected String doInBackground(String... args) {
            Log.d("Tweet Text", "> " + args[0]);
            String status = args[0];
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);

                // Access Token
                String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
                // Access Token Secret
                String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

                // Update status
                twitter4j.Status response = twitter.updateStatus(status);

                Log.d("Status", "> " + response.getText());
            } catch (TwitterException e) {
                // Error in updating status
                Log.d("Twitter Update Error", e.getMessage());
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog and show
         * the data in UI Always use runOnUiThread(new Runnable()) to update UI
         * from background thread, otherwise you will get error
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            // updating UI from Background Thread
        }


    }

    class LoginTwitter extends AsyncTask<Void, Boolean, Boolean> {

        /**
         * Before starting background thread Show Progress BaseDialog
         * */

        /**
         * getting Places JSON
         * */
        protected Boolean doInBackground(Void... args) {
            if (!isTwitterLoggedInAlready()) {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
                Configuration configuration = builder.build();

                TwitterFactory factory = new TwitterFactory(configuration);
                twitter = factory.getInstance();

                try {
                    requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                            .parse(requestToken.getAuthenticationURL())));
                } catch (TwitterException e) {
                    Log.e("asdsa", e.getMessage());
                }
            } else {
                // user already logged into twitter
                Toast.makeText(getApplicationContext(),
                        "Already Logged into twitter", Toast.LENGTH_LONG).show();
            }
            return true;
        }

        /**
         * After completing background task Dismiss the progress dialog and show
         * the data in UI Always use runOnUiThread(new Runnable()) to update UI
         * from background thread, otherwise you will get error
         * **/
        protected void onPostExecute() {

        }

    }

    /**
     * Function to logout from twitter
     * It will just clear the application shared preferences
     * */
    private void logoutFromTwitter() {
        // Clear the shared preferences
        Editor e = mSharedPreferences.edit();
        e.remove(PREF_KEY_OAUTH_TOKEN);
        e.remove(PREF_KEY_OAUTH_SECRET);
        e.remove(PREF_KEY_TWITTER_LOGIN);
        e.commit();

        // After this take the appropriate action
        // I am showing the hiding/showing buttons again
        // You might not needed this code
        btnLogoutTwitter.setVisibility(View.GONE);
        btnUpdateStatus.setVisibility(View.GONE);
        txtUpdate.setVisibility(View.GONE);
        lblUpdate.setVisibility(View.GONE);
        lblUserName.setText("");
        lblUserName.setVisibility(View.GONE);

        btnLoginTwitter.setVisibility(View.VISIBLE);
    }

    /**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     * */
    public boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }

}