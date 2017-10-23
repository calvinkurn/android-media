import React, { Component } from 'react'
import {
  View,
  Image,
  StyleSheet,
  Dimensions,
  Platform,
  TouchableHighlight
} from 'react-native'
import { NavigationModule } from 'NativeModules';


const { width, height } = Dimensions.get('window')

const MainBanner = ({ dataMainBanners }) => {
  const bannersImage = dataMainBanners.images
  // remapping array position
  let arrImages = bannersImage.reduceRight((prev, curr) => prev.concat(curr), [])

  return (
    <View style={styles.mainBannerContainer}>
      {
        arrImages.map((image, idx) => (
          idx === 0 ? null
          : <TouchableHighlight 
              underlayColor={'#FFF'}
              key={image.image_id}
              onPress={() => {
                console.log(image)
                NavigationModule.navigateWithMobileUrl('', image.destination_url, '')}}>
              <Image source={{uri: image.file_url}} style={idx === 1 ? styles.mainBannerImageBig : styles.mainBannerImageSmall} />
            </TouchableHighlight>
        ))
      }
    </View>
  )
}

const styles = StyleSheet.create({
  mainBannerContainer: {
    flex: 1,
    width: width,
    paddingRight: 15,
    paddingLeft: 15,
    marginBottom: 10,
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F8F8F8',
  },
  mainBannerImageBig: {
    width: width - 30,
    height: Math.round(width * .7),
    marginBottom: 10,
    ...Platform.select({
      ios: {
        resizeMode: 'contain'
      },
      android: {
        borderWidth: 1,
        borderRadius: 3,
        borderColor: '#E0E0E0',
        overlayColor: '#FFF',
      }
    })
  },
  mainBannerImageSmall: {
    width: width - 30,
    height: 140,
    marginBottom: 10,
    ...Platform.select({
      ios: {
        resizeMode: 'contain'
      },
      android: {
        borderWidth: 1,
        borderRadius: 3,
        borderColor: '#E0E0E0',
        overlayColor: '#FFF',
      }
    })
  }
})

export default MainBanner