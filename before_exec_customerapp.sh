# change config to bits
if grep 'resRepo=tokopedia' "gradle.properties"; then
  echo "changing resRepo to bits.."
  sed -i 's/resRepo=tokopedia/resRepo=bits/g' "gradle.properties"
fi

if [ -f "settings.gradle" ]; then
echo "disabling unused settings.."
sed -i 's|apply from: "buildconfig/appcompile/compile-sellerapp.gradle"||g' settings.gradle
sed -i 's|apply from: "buildconfig/appcompile/compile-customerapp_pro.gradle"||g' settings.gradle
sed -i 's|apply from: "buildconfig/appcompile/compile-testapp.gradle"||g' settings.gradle
fi
cat settings.gradle