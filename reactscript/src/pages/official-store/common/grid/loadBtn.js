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
import { icons } from '../../../../components/icons'
import iconLoadMore from '../../components/img/load-more.png'

const icon_arrowUp = 'https://firebasestorage.googleapis.com/v0/b/tokopedia-android.appspot.com/o/load-more.png?alt=media&token=bbc027ae-dea6-4a81-a319-776c4c3effbe'
// const icon_LoadMore = 'https://firebasestorage.googleapis.com/v0/b/tokopedia-android.appspot.com/o/load-more.png?alt=media&token=bbc027ae-dea6-4a81-a319-776c4c3effbe'
const LoadMore = ({ User_ID, onLoadMore, onSlideMore, offset, limit, canFetch, isFetching }) => {
  _onClick = () => {
    if (isFetching) {
      return
    } else {
      if (canFetch) {
        onLoadMore(limit, offset, User_ID, 'LOADANDREPLACE')
      } else {
        onSlideMore()
      }
    }
  }

  const Touchable = Platform.OS === 'android' ? TouchableNativeFeedback : TouchableOpacity
  
  return (
    <View style={{flex: 1 / 3}}>
      <Touchable onPress={_onClick}>
        <View style={styles.container}>
          <Image source={{ uri: icon_arrowUp }} style={{ flex:1, width:40, height:10, marginTop:30, marginBottom:5 }} />
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