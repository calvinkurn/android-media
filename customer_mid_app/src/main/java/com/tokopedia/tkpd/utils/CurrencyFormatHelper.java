package com.tokopedia.tkpd.utils;

import android.widget.EditText;

import java.text.NumberFormat;
import java.util.Locale;


public final class CurrencyFormatHelper {
	private static final NumberFormat RupiahFormat = NumberFormat.getCurrencyInstance(Locale.US);
	private static final NumberFormat DollarFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US"));;
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

	public static String ConvertToDollar(String string){
		try {
			String indollar;
			indollar = DollarFormat.format(Double.parseDouble(string)).replace("$", "");
			return indollar;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "Exception raised";
		}
	}

	public static void SetToRupiah(EditText et){
		try {
			if(et.length()>0 && !LockTextWatcher){
				LockTextWatcher = true;
				int tempCursorPos = et.getSelectionStart();
				int tempLength = et.length();
				et.setText(RupiahFormat.format(Long.parseLong(et.getText().toString().replace("$", "").replace(".00", "").replace(".0", "").replace(",", "").replace(".", ""))).replace("$", "").replace(".00", "").replace(".0", ""));
				// Handler untuk tanda koma
				if(et.length() - tempLength == 1) // Untuk majuin cursor ketika nambah koma
				{
					if(et.length()<4)
						tempCursorPos += 1;
					else if(et.getText().charAt(tempCursorPos) != '.') // Untuk mundur ketika mencoba menghapus koma
						tempCursorPos += 1;
				}
				else if(et.length() - tempLength == -1) // Mundurin cursor ketika hapus koma
				{
					tempCursorPos -= 1;
				}
				else if(et.length()>3 && tempCursorPos<et.length() && tempCursorPos>0)
					if(et.getText().charAt(tempCursorPos-1) == '.'){ // Mundurin cursor ketika menambah digit dibelakang koma
						tempCursorPos -= 1;
					}

				// Set posisi cursor
				if(tempCursorPos<et.length() && tempCursorPos > -1)
					et.setSelection(tempCursorPos);
				else if(tempCursorPos<0)
					et.setSelection(0);
				else
					et.setSelection(et.length());
				LockTextWatcher = false;
			}
		} catch (NumberFormatException e) {
			LockTextWatcher = false;
			e.printStackTrace();
		}
	}

	public static void setToRupiahCheckPrefix(EditText et){
		try {
			int noFirstCharToIgnore = countPrefixCurrency(et.getText().toString());
			if(et.length()>noFirstCharToIgnore && !LockTextWatcher){
				LockTextWatcher = true;
				int tempCursorPos = et.getSelectionStart();
				int tempLength = et.length();
				et.setText(RupiahFormat.format(Long.parseLong(
						et.getText().toString()
								.substring(noFirstCharToIgnore)
								.replace("$", "")
								.replace(".00", "")
								.replace(".0", "")
								.replace(",", "")
								.replace(".", "")))
								.replace("$", "")
								.replace(".00", "")
								.replace(".0", ""));
				// Handler untuk tanda koma
				if(et.length() - tempLength == 1) // Untuk majuin cursor ketika nambah koma
				{
					if(et.length()< (4 + noFirstCharToIgnore)) {
						tempCursorPos += 1;
					}
					else if(et.getText().charAt(tempCursorPos) != '.') { // Untuk mundur ketika mencoba menghapus koma
						tempCursorPos += 1;
					}
				}
				else if(et.length() - tempLength == -1) // Mundurin cursor ketika hapus koma
				{
					tempCursorPos -= 1;
				}
				else if(et.length()>(3 + noFirstCharToIgnore)
						&& tempCursorPos < et.length()
						&& tempCursorPos > noFirstCharToIgnore) {
					if (et.getText().charAt(tempCursorPos - 1) == '.') { // Mundurin cursor ketika menambah digit dibelakang koma
						tempCursorPos -= 1;
					}
				}

				// Set posisi cursor
				if(tempCursorPos < et.length() && tempCursorPos > (-1+noFirstCharToIgnore) )
					et.setSelection(tempCursorPos);
				else if(tempCursorPos< noFirstCharToIgnore )
					et.setSelection(noFirstCharToIgnore);
				else
					et.setSelection(et.length());
				LockTextWatcher = false;
			}
		} catch (NumberFormatException e) {
			LockTextWatcher = false;
			e.printStackTrace();
		}
	}

	public static int countPrefixCurrency(String string) {
		int count = 0;
		for (int i=0, sizei = string.length();i<sizei ; i++) {
			char charString = string.charAt(i);
			if (Character.isDigit(charString)){
				break;
			}
			else {
				count++;
			}
		}
		return count;
	}

	/**
	 * Use StringUtils instead
	 * @param string
	 * @return
	 */
	@Deprecated
	public static String RemoveNonNumeric(String string){
		String numeric;
		numeric = string.replace(",", "");
		return numeric;
	}

	public static String removeCurrencyPrefix(String string){
		if (string == null) return null;
		int count = 0;
		for (int i=0, sizei = string.length();i<sizei ; i++) {
			char charString = string.charAt(i);
			if (Character.isDigit(charString)){
				break;
			}
			else {
				count++;
			}
		}
		return string.substring(count);
	}

    public static int convertRupiahToInt(String rupiah) {
        rupiah = rupiah.replace("Rp", "");
        rupiah = rupiah.replace(".", "");
        rupiah = rupiah.replace(" ", "");
        return Integer.parseInt(rupiah);
    }

	public static long convertRupiahToLong(String rupiah) {
		rupiah = rupiah.replace("Rp", "");
		rupiah = rupiah.replace(".", "");
		rupiah = rupiah.replace(" ", "");
		return Long.parseLong(rupiah);
	}
}
