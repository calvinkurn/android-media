import React from 'react'
import {
  View,
  Image,
  Button,
  Text,
  TouchableNativeFeedback,
  TouchableOpacity,
  Platform,
  StyleSheet } from 'react-native'
import PropTypes from 'prop-types'
import { NavigationModule } from 'NativeModules'


const LoadMore = ({ onLoadMore, onSlideMore, offset, limit, canFetch, isFetching }) => {
  const Touchable = Platform.OS === 'android' ? TouchableNativeFeedback : TouchableOpacity

  _onClick = () => {
    NavigationModule.getCurrentUserId().then(uuid => {
      if (isFetching) {
        return
      } else {
        if (canFetch) {
          onLoadMore(limit, offset, uuid, 'LOADANDREPLACE')
        } else {
          onSlideMore()
        }
      }
    })
  }
  
  return (
    <View style={{flex: 1 / 3}}>
      <Touchable onPress={_onClick}>
        <View style={styles.container}>
          <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_offstore/load_more.png' }} style={{ flex:1, width:35, height:10, marginTop:30, marginBottom:5 }} />
          <Text style={styles.text}>Brand Lainnya</Text>
        </View>
      </Touchable>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
  },
  text: {
    color: '#42b549',
  }
})

export default LoadMore