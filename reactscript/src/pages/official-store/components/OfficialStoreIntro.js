import React from 'react'
import { View, Text, Image, FlatList, StyleSheet, Dimensions } from 'react-native'
//import { icons } from '../../../icons/index'

const { width } = Dimensions.get('window')

// // Icon from Firebase
// const iconCheck = 'https://firebasestorage.googleapis.com/v0/b/tokopedia-android.appspot.com/o/icon-gcheck.png?alt=media&token=a0a1e5de-c41e-4bd8-aa7e-9e8bfa98d518'
// const iconUSP = 'https://firebasestorage.googleapis.com/v0/b/tokopedia-android.appspot.com/o/icon-usp.png?alt=media&token=410c02e3-30c4-41f8-a24c-faf425286131'


// Get Width of View Component
getWidthofView = (event) => {
  this.viewWidth =  Math.floor(event.nativeEvent.layout.width)
}

const OfficialStoreIntro = () => {
  const uspText = [
    'Produk dari Brand Resmi',
    'Penawaran Promo Ekslusif',
    'Pelayanan Berkualitas',
    'Cicilan 0% Gratis Biaya Admin'
  ]

  return (
    <View style={styles.osIntro}>
      <View style={styles.osIntroInner}>
        <Image source={{ uri : 'https://ecs7.tokopedia.net/img/android_offstore/icon-usp.png' }} style={styles.osIntroImage}/>
        <View style={styles.osIntroTextWrap}>

          <Text style={styles.uspHeadingTitle}>{'Official Store Tokopedia'.toUpperCase()}</Text>
          <View style={styles.uspTextInner} onLayout={this.getWidthofView}>
          {
            uspText.map((usp, idx) => (
              <View key={idx} style={styles.uspText}>
                <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_offstore/icon-gcheck.png' }} style={{width: 10, height: 15, resizeMode: 'contain', marginRight: 5, marginLeft: 5}}/>
                <Text numberOfLines={2} style={styles.uspTextContent}>{usp}</Text>
              </View>
            ))
          }
          </View>
        </View>
      </View>
    </View>
  )
}

const styles = StyleSheet.create({
  osIntro: {
    flex: 1,
    backgroundColor: '#FFF',
    marginBottom: 20,
    padding: 10,
    borderBottomWidth: 1,
    borderColor: '#E0E0E0',
  },
  osIntroInner: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  osIntroImage: {
    flex: 1 / 3,
    justifyContent: 'center',
    alignItems: 'center',
    height: 70,
    resizeMode: 'contain',
  },
  osIntroTextWrap: {
    flex: 1,
  },
  uspHeadingTitle: {
    fontSize: 16,
    fontWeight: '700',
    color: '#42B549',
    fontStyle: 'italic',
    marginBottom: 5,
  },
  uspTextInner: {
    flex: 1,
    flexDirection: 'row',
    flexWrap: 'wrap',
  },
  uspText: {
    //width: this.viewWidth / 2, // If using nativeEvent
    width: Math.floor(width / 2.9),
    flexDirection: 'row',
    alignItems: 'flex-start',
    flexWrap: 'nowrap',
  },
  uspTextContent: {
    //width: this.viewWidth / 2.3, // If using nativeEvent
    width: width / 3,
    fontSize: 11,
    lineHeight: 15,
  }
})

export default OfficialStoreIntro