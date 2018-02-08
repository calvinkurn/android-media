import React from 'react'
import PropsTypes from 'prop-types'
import {
  View,
  Modal,
  StyleSheet,
  Dimensions,
  Text,
  Button,
  Image,
  TouchableWithoutFeedback,
} from 'react-native'
const { width, height } = Dimensions.get('window')

const TKPPopupModal = ({
  visible,
  onBackPress,
  title,
  subTitle,
  firstOptionText,
  secondOptionText,
  onFirstOptionTap,
  onSecondOptionTap,
  animationType,
  onCloseIconTap,
}) => {
  return (
    <Modal
      animationType={animationType}
      transparent={true}
      hardwareAccelerated={true}
      visible={visible}
      onRequestClose={onBackPress} >
      <View style={styles.container}>
        <View style={styles.overlay}>
          <View style={styles.box}>
            <View style={styles.padVer5}>
              <Text style={styles.titleText}>{title}</Text>
            </View>
            {
              subTitle && (<View style={styles.padVer5}>
                <Text style={styles.subTitleText}>{subTitle}</Text>
              </View>)
            }
            <View style={styles.actionContainer}>
              <View>
                <TouchableWithoutFeedback
                  onPress={onFirstOptionTap}>
                  <View style={styles.firstTextContainer}>
                    <Text style={styles.firstTextStyle}>{firstOptionText}</Text>
                  </View>
                </TouchableWithoutFeedback>
              </View>
              <View>
                <TouchableWithoutFeedback
                  onPress={onSecondOptionTap}>
                  <View style={styles.secondContainer}>
                    <Text style={styles.secondTextStyle}>{secondOptionText}</Text>
                  </View>
                </TouchableWithoutFeedback>
              </View>
            </View>
          </View>
        </View>
      </View>
    </Modal>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.5)',
  },
  overlay: {
    flex: 1,
    position: 'absolute',
    left: 0,
    top: 0,
    width: width,
    height: height,
    justifyContent: 'center',
    alignItems: 'center',
  },
  box: {
    backgroundColor: '#fff',
    width: '80%',
    borderRadius: 9,
    height: 189,
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
  },
  closeIcon: {
    alignSelf: 'flex-end',
    right: 10,
  },
  titleContainer: {

  },
  titleText: {
    fontSize: 20,
    fontWeight: '600',
    color: 'rgba(0, 0, 0, 0.7)',
    fontFamily: 'Roboto-Medium',
  },
  subTitleText: {
    fontSize: 14,
    color: 'rgba(0, 0, 0, 0.54)',
    fontWeight: '600',
    marginTop: 10
  },
  actionContainer: {
    flexDirection: 'row',
    marginVertical: 25
  },
  firstTextContainer: {
    height: 40,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 3,
    borderWidth: 1,
    borderColor: '#e0e0e0',
    width: 145,
    marginHorizontal: 5,
  },
  firstTextStyle: {
    fontSize: 13,
    fontWeight: '600',
    color: 'rgba(0, 0, 0, 0.54)'
  },
  secondContainer: {
    height: 40,
    backgroundColor: '#42b549',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 3,
    borderWidth: 1,
    borderColor: '#42b549',
    width: 145,
  },
  secondTextStyle: {
    fontSize: 13,
    fontWeight: '600',
    color: '#fff'
  },
  padVer5: {
    paddingVertical: 5
  }
})

TKPPopupModal.defaultProps = {
  visible: false,
  onBackPress: () => { },
  title: 'Please provide title',
  firstOptionText: 'Tidak',
  secondOptionText: 'Ya',
  onFirstOptionTap: () => { },
  onSecondOptionTap: () => { },
  animationType: 'fade',
  onCloseIconTap: () => { },
};

TKPPopupModal.propTypes = {
  visible: PropsTypes.bool.isRequired,
  onBackPress: PropsTypes.func.isRequired,
  title: PropsTypes.string.isRequired,
  subTitle: PropsTypes.string,
  firstOptionText: PropsTypes.string.isRequired,
  secondOptionText: PropsTypes.string.isRequired,
  onFirstOptionTap: PropsTypes.func.isRequired,
  onSecondOptionTap: PropsTypes.func.isRequired,
  animationType: PropsTypes.string.isRequired,
  onCloseIconTap: PropsTypes.func.isRequired,
}

export default TKPPopupModal