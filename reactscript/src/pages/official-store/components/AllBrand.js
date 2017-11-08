import React from 'react'
import { Image, TouchableWithoutFeedback } from 'react-native'
import { NavigationModule } from 'NativeModules';

const redirectUrl = 'tokopedia://webview?url=' + encodeURIComponent('https://m.tokopedia.com/official-store/brand')

const AllBrands = () => (
  <TouchableWithoutFeedback onPress={() => NavigationModule.navigateWithMobileUrl('', redirectUrl, '')}>
    <Image
      source={{ uri: 'https://ecs7.tokopedia.net/assets-tokopedia-lite/prod/img/banner-all-brand.51fcc6194dba5cf044bf5f895dc3b9d5.jpg' }}
      style={{ width: '100%', height: 105, resizeMode: 'cover' }}
    />
  </TouchableWithoutFeedback>
)

export default AllBrands