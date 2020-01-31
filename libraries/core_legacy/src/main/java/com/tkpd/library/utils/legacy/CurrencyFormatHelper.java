package com.tkpd.library.utils.legacy;

import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * User {@link com.tokopedia.design.utils.CurrencyFormatHelper} or {@link com.tokopedia.design.utils.CurrencyFormatUtil}
 * modified by m.normansyah & steven.f
 * changed "," to "." for rupiah
 */
@Deprecated
public final class CurrencyFormatHelper {
	private static final NumberFormat RupiahFormat = NumberFormat.getCurrencyInstance(Locale.US);
	private static final NumberFormat DollarFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
	// this flag intend to block textwatcher to be called recursively
	private static boolean LockTextWatcher = false;

	public static final int RUPIAH = 1;
	public static final int OTHER = -1;


	public static String ConvertToRupiah(String string){
		try{
			String inrupiah;
			inrupiah = RupiahFormat.format(Long.parseLong(string.replace("Rp", ""))).replace("$", "").replace("Rp", "").replace(".00", "");
			return inrupiah;
		}
		catch(NumberFormatException e){
			e.printStackTrace();
			return "Exception raised";
		}
	}

    public static int convertRupiahToInt(String rupiah) {
		try {
        	rupiah = rupiah.replace("Rp", "");
        	rupiah = rupiah.replace(".", "");
        	rupiah = rupiah.replace(" ", "");
			return Integer.parseInt(rupiah);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
    }
}
