package com.tkpd.library.utils;

/**
 * Created by Steven on 14/12/2015.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core2.R;

public class ConnectionDetector {

    private Context context;

    public ConnectionDetector(){

    }

    public ConnectionDetector(Context context){
        this.context = context;
    }

    /**
     * Checking for all possible internet providers
     * **/
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
        }
        return false;
    }

    public void showNoConnection(){
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.error_network_dialog, null);
        TextView msg = (TextView) promptsView.findViewById(R.id.msg);
        String noConnection = context.getResources().getString(R.string.msg_no_connection) + ".\n"
                + context.getResources().getString(R.string.error_no_connection2) + ".";
        msg.setText(noConnection);
        myAlertDialog.setView(promptsView);
        myAlertDialog.show();
    }
}